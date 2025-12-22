package com.raf.marketplace.data.local.room.cart

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Upsert
    suspend fun upsertItem(item: CartEntity)

    @Query("SELECT * FROM cart ORDER BY timestamp DESC")
    fun getAllItems(): Flow<List<CartEntity>>

    @Query("SELECT COUNT(*) FROM cart")
    fun getItemCount(): Flow<Int>

    @Query("DELETE FROM cart WHERE productId = :productId")
    suspend fun deleteItemByProductId(productId: Int)

    @Query("DELETE FROM cart")
    suspend fun deleteAllItems()
}