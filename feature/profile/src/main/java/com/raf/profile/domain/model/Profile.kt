package com.raf.profile.domain.model

import kotlinx.serialization.Serializable

data class Profile(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val name: ProfileName,
    val phone: String,
    val address: ProfileAddress,
)

@Serializable
data class ProfileAddress(
    val geolocation: ProfileGeolocation,
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String,
)

@Serializable
data class ProfileGeolocation(
    val lat: String,
    val long: String,
)

@Serializable
data class ProfileName(
    val firstname: String,
    val lastname: String,
)
