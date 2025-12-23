package com.raf.core.domain.contract

import com.raf.core.domain.model.ApiResult
import com.raf.core.domain.model.Profile

interface ProfileProvider {
    suspend fun getUserById(token: String, userId: Int): ApiResult<Profile>
}