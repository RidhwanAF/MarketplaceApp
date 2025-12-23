package com.raf.core.domain.usecase

import com.raf.core.domain.contract.AuthProvider

class GetUserIdUseCase(private val authProvider: AuthProvider) {
    suspend operator fun invoke() = authProvider.getUserId()
}