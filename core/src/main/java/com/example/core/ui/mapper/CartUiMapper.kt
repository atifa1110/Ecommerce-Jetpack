package com.example.core.ui.mapper

import com.example.core.domain.model.CartModel
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.ui.model.Cart
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import com.example.core.ui.model.Wishlist

fun Cart.asItemTransactionModel() = ItemTransactionModel(
    productId = productId,
    variantName = variantName,
    quantity = quantity
)

fun List<Cart>.asItemTransactionModel() = this.map { it.asItemTransactionModel() }

fun CartModel.asCart() = Cart(
    productId = productId,
    productName = productName,
    productImage = image,
    variantName = variantName,
    stock = stock,
    unitPrice = unitPrice,
    quantity = quantity,
    isCheck = false
)

fun Wishlist.asCartModel(unitPrice: Int,variantName:String) = CartModel(
    productId = productId,
    productName = productName,
    unitPrice = unitPrice,
    image = productImage,
    variantName = variantName,
    quantity = 1,
    stock = stock
)

fun ProductDetail.asCartModel(selectedVariant : ProductVariant) = CartModel(
    productId = productId?:"",
    productName = productName?:"",
    unitPrice = (this.productPrice ?: 0) + selectedVariant.variantPrice,
    image = image?.get(0)?:"",
    variantName = selectedVariant.variantName,
    quantity = 1,
    stock = stock?:0
)

fun ProductDetail.asCart(selectedVariant : ProductVariant) = Cart(
    productId = productId?:"",
    productName = productName?:"",
    unitPrice = (this.productPrice ?: 0) + selectedVariant.variantPrice,
    productImage = image?.get(0)?:"",
    variantName = selectedVariant.variantName,
    quantity = 1,
    stock = stock?:0
)