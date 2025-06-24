package com.example.core.data.mapper

import com.example.core.domain.model.CartModel
import com.example.core.data.local.room.entity.cart.CartEntity

fun CartEntity.asCartModel() = CartModel(
    productId = productId,
    productName = productName,
    image = image,
    variantName = variantName,
    stock = stock,
    unitPrice = unitPrice,
    quantity = quantity
)

fun CartModel.asCartEntity() = CartEntity(
    productId = productId,
    productName = productName,
    image = image,
    variantName = variantName,
    stock = stock,
    unitPrice = unitPrice,
    quantity = quantity
)