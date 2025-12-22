package com.raf.marketplace.domain.usecase.cart

import com.raf.marketplace.domain.model.Cart
import com.raf.marketplace.domain.repository.MarketplaceRepository

class AddToCartUseCase(private val marketplaceRepository: MarketplaceRepository) {
    suspend operator fun invoke(cart: Cart) = marketplaceRepository.addToCart(cart)
}