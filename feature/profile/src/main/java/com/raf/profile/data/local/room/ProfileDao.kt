package com.raf.profile.data.local.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ProfileDao {

    @Upsert
    suspend fun upsertProfile(profile: ProfileEntity)

    @Query("DELETE FROM profile WHERE id = :id")
    suspend fun deleteProfileById(id: Int)

    @Query("SELECT * FROM profile WHERE id = :id")
    suspend fun getProfileById(id: Int): ProfileEntity?
}