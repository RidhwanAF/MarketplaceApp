package com.raf.profile.data.repository

import android.content.Context
import android.util.Log
import com.raf.core.data.utility.EncryptionManager
import com.raf.core.data.utility.NetworkHelper
import com.raf.core.domain.contract.ProfileProvider
import com.raf.core.domain.model.ApiResult
import com.raf.core.domain.model.Profile
import com.raf.profile.data.local.room.ProfileDatabase
import com.raf.profile.data.remote.ProfileApiService
import com.raf.profile.data.repository.mapper.ProfileMapper.toDomain
import com.raf.profile.data.repository.mapper.ProfileMapper.toEntity
import com.raf.profile.domain.repository.ProfileRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    profileDb: ProfileDatabase,
    private val apiService: ProfileApiService,
) : ProfileRepository, ProfileProvider {

    private val dao = profileDb.dao

    override suspend fun getUserById(token: String, userId: Int): ApiResult<Profile> {
        return try {
            if (!NetworkHelper.isNetworkAvailable(context)) {
                return getProfileFromLocalIfAvailable(userId)
            }
            val response = apiService.fetchProfile("Bearer $token", userId)
            if (response.isSuccessful) {
                val profile =
                    response.body()?.toEntity() ?: return getProfileFromLocalIfAvailable(userId)

                val encryptedPassword =
                    EncryptionManager.encrypt(profile.password) ?: profile.password

                dao.upsertProfile(profile.copy(password = encryptedPassword))
                ApiResult.Success(profile.toDomain())
            } else {
                val errorMessage = response.errorBody()?.string()?.takeIf { it.isNotEmpty() }
                    ?: response.message()
                Log.e(TAG, "Failed to fetch Profile: $errorMessage")
                getProfileFromLocalIfAvailable(userId)
            }
        } catch (e: Exception) {
            Log.e(TAG, "getUserById: ", e)
            ApiResult.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    private suspend fun getProfileFromLocalIfAvailable(userId: Int): ApiResult<Profile> {
        return try {
            val profile = dao.getProfileById(userId)
            if (profile != null) {
                val decryptedPassword =
                    EncryptionManager.decrypt(profile.password) ?: profile.password

                ApiResult.Success(profile.copy(password = decryptedPassword).toDomain())
            } else {
                ApiResult.Error("Profile not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "getProfileFromLocalIfAvailable: ", e)
            ApiResult.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    override suspend fun deleteUserProfileById(userId: Int) {
        try {
            dao.deleteProfileById(userId)
        } catch (e: Exception) {
            Log.e(TAG, "deleteUserProfileById: ", e)
        }
    }

    private companion object {
        private const val TAG = "ProfileRepositoryImpl"
    }
}