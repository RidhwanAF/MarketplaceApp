package com.raf.auth.domain.repository

import com.raf.auth.domain.model.AuthResult

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult<String>
    suspend fun register(username: String, password: String): AuthResult<String>
}