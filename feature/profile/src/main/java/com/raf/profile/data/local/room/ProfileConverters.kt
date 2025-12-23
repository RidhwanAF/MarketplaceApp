package com.raf.profile.data.local.room

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class ProfileConverters {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @TypeConverter
    fun fromNameEntity(name: NameEntity): String {
        return json.encodeToString(name)
    }

    @TypeConverter
    fun toNameEntity(nameString: String): NameEntity {
        return json.decodeFromString(nameString)
    }

    @TypeConverter
    fun fromAddressEntity(address: AddressEntity): String {
        return json.encodeToString(address)
    }

    @TypeConverter
    fun toAddressEntity(addressString: String): AddressEntity {
        return json.decodeFromString(addressString)
    }
}