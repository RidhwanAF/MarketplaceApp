package com.raf.marketplace.data.repository.mapper

import com.raf.marketplace.data.local.room.cart.CartEntity
import com.raf.marketplace.domain.model.Cart

object CartMapper {

    fun CartEntity.toDomain() = Cart(
        productId = productId,
        quantity = quantity,
    )

    fun Cart.toEntity() = CartEntity(
        productId = productId,
        quantity = quantity,
        timestamp = System.currentTimeMillis()
    )
}