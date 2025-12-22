package com.raf.marketplace.domain.usecase.cart

import com.raf.marketplace.domain.repository.MarketplaceRepository

class GetItemCountFromCartUseCase(private val marketplaceRepository: MarketplaceRepository) {
    operator fun invoke() = marketplaceRepository.getItemCountFromCart()
}