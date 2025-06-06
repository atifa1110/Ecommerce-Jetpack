package com.example.ecommerceapp.data.domain.mapper

import com.example.ecommerceapp.data.domain.CartModel
import com.example.ecommerceapp.data.local.room.entity.cart.CartEntity


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