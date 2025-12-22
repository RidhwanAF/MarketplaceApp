package com.raf.marketplace.data.local.room.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey
    val productId: Int,
    val quantity: Int,
    val timestamp: Long = System.currentTimeMillis(),
)
