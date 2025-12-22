package com.raf.marketplace.domain.usecase.product

import com.raf.marketplace.domain.repository.MarketplaceRepository

class GetProductCategoriesUseCase(private val marketplaceRepository: MarketplaceRepository) {
    operator fun invoke() = marketplaceRepository.getProductCategories()
}