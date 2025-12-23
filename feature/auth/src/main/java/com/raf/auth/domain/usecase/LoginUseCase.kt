package com.raf.auth.domain.usecase

import com.raf.auth.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(id: Int, username: String, password: String) =
        authRepository.login(id, username, password)
}