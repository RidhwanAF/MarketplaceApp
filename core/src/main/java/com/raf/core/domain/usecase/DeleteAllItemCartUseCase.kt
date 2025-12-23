package com.raf.core.domain.usecase

import com.raf.core.domain.contract.CartProvider

class DeleteAllItemCartUseCase(private val cartProvider: CartProvider) {
    suspend operator fun invoke() = cartProvider.deleteAllItemFromCart()
}