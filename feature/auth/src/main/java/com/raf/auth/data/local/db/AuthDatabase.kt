package com.raf.auth.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AuthEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AuthDatabase : RoomDatabase() {
    abstract val authDao: AuthDao
}