package com.raf.profile.domain.repository

interface ProfileRepository {
    suspend fun deleteUserProfileById(userId: Int)
}