package com.raf.auth.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth")
data class AuthEntity(
    @PrimaryKey val id: String,
    val username: String,
    val password: String,
)
