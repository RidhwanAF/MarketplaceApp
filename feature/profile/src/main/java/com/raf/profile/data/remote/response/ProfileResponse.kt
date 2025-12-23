package com.raf.profile.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val name: NameResponse,
    val phone: String,
    val address: AddressResponse,
)

@Serializable
data class AddressResponse(
    val geolocation: GeolocationResponse,
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String,
)

@Serializable
data class GeolocationResponse(
    val lat: String,
    val long: String,
)

@Serializable
data class NameResponse(
    val firstname: String,
    val lastname: String,
)
