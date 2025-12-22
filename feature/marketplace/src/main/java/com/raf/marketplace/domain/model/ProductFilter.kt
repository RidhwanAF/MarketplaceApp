package com.raf.marketplace.domain.model

data class ProductFilter(
    val query: String = "",
    val categories: List<String> = emptyList(),
    val productSortTypes: List<Pair<ProductSortType, Boolean>> = emptyList(),
)

enum class ProductSortType {
    NAME,
    PRICE,
    RATING,
}
