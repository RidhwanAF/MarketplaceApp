package com.raf.profile.domain.usecase

import com.raf.profile.domain.repository.ProfileRepository

class GetUserProfileUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(token: String, userId: Int) =
        profileRepository.getUserById(token, userId)
}