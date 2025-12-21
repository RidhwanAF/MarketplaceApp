package com.raf.auth.data.repository

import android.util.Log
import com.raf.auth.R
import com.raf.auth.data.local.AuthDataStore
import com.raf.auth.data.local.db.AuthDatabase
import com.raf.auth.data.local.db.AuthEntity
import com.raf.auth.domain.model.AuthResult
import com.raf.auth.domain.repository.AuthRepository
import com.raf.core.data.utility.EncryptionManager
import com.raf.core.domain.contract.AuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    authDb: AuthDatabase,
    private val authDataStore: AuthDataStore,
) : AuthRepository, AuthProvider {

    private companion object {
        private const val TAG = "AuthRepositoryImpl"
    }

    private val dao = authDb.authDao

    override suspend fun login(
        username: String,
        password: String,
    ): AuthResult<String> {
        try {
            val authEntity = dao.getAuthByUsername(username)
                ?: return AuthResult.Error(R.string.user_not_found_please_register_first)

            // Validate Login
            return if (authEntity.username != username ||
                !EncryptionManager.compareIsSame(password, authEntity.password)
            ) {
                AuthResult.Error(R.string.invalid_username_or_password)
            } else {
                val userId = authEntity.id
                val encryptedToken = EncryptionManager.encrypt(userId)
                    ?: return AuthResult.Error(R.string.failed_to_login)

                authDataStore.saveSessionToken(encryptedToken)
                AuthResult.Success(authEntity.username)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to login", e)
            return AuthResult.Error(R.string.failed_to_login)
        }
    }

    override suspend fun register(
        username: String,
        password: String,
    ): AuthResult<String> {
        try {
            // Validate User
            val isUserExist = dao.getAuthByUsername(username) != null
            if (isUserExist) {
                return AuthResult.Error(R.string.user_already_exist)
            }

            // Register User
            val encryptedPassword = EncryptionManager.encrypt(password)
                ?: return AuthResult.Error(R.string.failed_to_register)
            val userId = UUID.randomUUID().toString()
            val newAuthEntity = AuthEntity(
                id = userId,
                username = username,
                password = encryptedPassword,
            )
            dao.insertAuth(newAuthEntity)
            return AuthResult.Success(userId)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register", e)
            return AuthResult.Error(R.string.failed_to_register)
        }
    }

    override fun getAuthToken(): Flow<String?> {
        return try {
            authDataStore.getSessionToken().map { encryptedToken ->
                if (encryptedToken == null) return@map null
                EncryptionManager.decrypt(encryptedToken)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get auth token", e)
            flowOf(null)
        }
    }

    override suspend fun logout() {
        try {
            authDataStore.clearSessionToken()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to logout", e)
        }
    }
}