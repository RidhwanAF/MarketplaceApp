package com.raf.auth.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthDataStore @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val Context.dataStore by preferencesDataStore("auth_data_preferences")

    private val sessionTokenKey = stringPreferencesKey("session_token_key")
    private val userIdKey = stringPreferencesKey("user_id_key")

    suspend fun saveSession(token: String, userId: String) {
        context.dataStore.edit { preferences ->
            preferences[sessionTokenKey] = token
            preferences[userIdKey] = userId
        }
    }

    fun getSessionToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[sessionTokenKey]
        }
    }

    fun getUserId(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[userIdKey]
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { preferences ->
            preferences.remove(sessionTokenKey)
            preferences.remove(userIdKey)
        }
    }
}