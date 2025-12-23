package com.raf.profile.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [ProfileEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ProfileConverters::class)
abstract class ProfileDatabase : RoomDatabase() {
    abstract val dao: ProfileDao
}