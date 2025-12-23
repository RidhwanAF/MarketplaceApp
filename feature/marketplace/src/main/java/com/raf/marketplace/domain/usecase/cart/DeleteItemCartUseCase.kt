package com.raf.marketplace.domain.usecase.cart

import com.raf.marketplace.domain.repository.MarketplaceRepository

class DeleteItemCartUseCase(private val marketplaceRepository: MarketplaceRepository) {
    suspend operator fun invoke(productId: Int) =
        marketplaceRepository.deleteItemCartByProductId(productId)
}