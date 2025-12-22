package com.raf.marketplace.domain.usecase.cart

import com.raf.marketplace.domain.repository.MarketplaceRepository

class DeleteAllItemFromCartUseCase(private val marketplaceRepository: MarketplaceRepository) {
    suspend operator fun invoke() = marketplaceRepository.deleteAllItemFromCart()
}