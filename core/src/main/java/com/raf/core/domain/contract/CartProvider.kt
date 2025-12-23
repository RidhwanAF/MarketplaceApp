package com.raf.core.domain.contract

interface CartProvider {
    suspend fun deleteAllItemFromCart(): Result<Unit>
}