package com.raf.profile.domain.repository

import com.raf.core.domain.model.ApiResult
import com.raf.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getUserById(token: String, userId: Int): ApiResult<Profile>
    suspend fun deleteUserProfileById(userId: Int)
}