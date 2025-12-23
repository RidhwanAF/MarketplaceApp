package com.raf.profile.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val name: NameEntity,
    val phone: String,
    val address: AddressEntity,
)

@Serializable
data class AddressEntity(
    val geolocation: GeolocationEntity,
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String,
)

@Serializable
data class GeolocationEntity(
    val lat: String,
    val long: String,
)

@Serializable
data class NameEntity(
    val firstname: String,
    val lastname: String,
)
