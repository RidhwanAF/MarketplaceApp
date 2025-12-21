package com.raf.auth.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuth(authEntity: AuthEntity)

    @Query("SELECT * FROM auth WHERE username = :username")
    suspend fun getAuthByUsername(username: String): AuthEntity?
}