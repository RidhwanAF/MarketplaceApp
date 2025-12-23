package com.raf.core.domain.contract

import kotlinx.coroutines.flow.Flow

interface AuthProvider {
    fun getAuthToken(): Flow<String?>
    suspend fun getUserId(): Int?
    suspend fun logout()
}