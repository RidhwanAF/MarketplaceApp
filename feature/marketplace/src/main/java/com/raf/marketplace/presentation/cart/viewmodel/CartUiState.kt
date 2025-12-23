package com.raf.marketplace.presentation.cart.viewmodel

import com.raf.core.domain.model.Profile
import com.raf.marketplace.domain.model.Cart
import com.raf.marketplace.domain.model.Product

data class CartUiState(
    val isLoading: Boolean = false,
    val productsInCart: List<ProductInCartUi> = emptyList(),
    val profile: Profile? = null,
    val totalPriceInDollar: Double = 0.0,
    val totalQuantity: Int = 0,
    val uiMessage: String? = null,
    val removingItemById: Int? = null,
    val checkoutSimulation: Boolean = false,
)

data class ProductInCartUi(
    val product: Product,
    val cart: Cart,
)
