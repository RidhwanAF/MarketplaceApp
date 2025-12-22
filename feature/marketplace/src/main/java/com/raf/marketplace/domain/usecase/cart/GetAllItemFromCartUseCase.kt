package com.raf.marketplace.domain.usecase.cart

import com.raf.marketplace.domain.repository.MarketplaceRepository

class GetAllItemFromCartUseCase(private val marketplaceRepository: MarketplaceRepository) {
    operator fun invoke() = marketplaceRepository.getAllItemFromCart()
}