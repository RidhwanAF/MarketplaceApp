package com.raf.core.domain.usecase

import com.raf.core.domain.contract.AuthProvider

class GetAuthTokenUseCase(private val authProvider: AuthProvider) {
    operator fun invoke() = authProvider.getAuthToken()
}