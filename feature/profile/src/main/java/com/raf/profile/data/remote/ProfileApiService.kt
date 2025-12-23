package com.raf.profile.data.remote

import com.raf.profile.data.remote.response.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProfileApiService {

    @GET("users/{id}")
    suspend fun fetchProfile(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
    ): Response<ProfileResponse>

}