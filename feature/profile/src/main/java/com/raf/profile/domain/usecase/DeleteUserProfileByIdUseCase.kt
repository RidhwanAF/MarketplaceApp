package com.raf.profile.domain.usecase

import com.raf.profile.domain.repository.ProfileRepository

class DeleteUserProfileByIdUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(userId: Int) = profileRepository.deleteUserProfileById(userId)
}