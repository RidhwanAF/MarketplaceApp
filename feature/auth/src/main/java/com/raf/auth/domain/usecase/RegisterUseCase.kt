package com.raf.auth.domain.usecase

import com.raf.auth.domain.repository.AuthRepository

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(username: String, password: String) =
        authRepository.register(username, password)
}