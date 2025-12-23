package com.raf.core.domain.usecase

import com.raf.core.domain.contract.ProfileProvider

class GetUserProfileUseCase(private val profileProvider: ProfileProvider) {
    suspend operator fun invoke(token: String, userId: Int) =
        profileProvider.getUserById(token, userId)
}