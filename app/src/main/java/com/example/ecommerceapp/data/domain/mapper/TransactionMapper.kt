package com.example.ecommerceapp.data.domain.mapper

import com.example.ecommerceapp.data.domain.ItemTransactionModel
import com.example.ecommerceapp.data.domain.TransactionModel
import com.example.ecommerceapp.data.network.model.ItemTransactionNetwork
import com.example.ecommerceapp.data.network.model.TransactionNetwork

fun TransactionNetwork.asTransactionModel() = TransactionModel(
    invoiceId = invoiceId,
    status = status,
    date = date,
    time = time,
    payment = payment,
    total = total,
    items = items.asItemTransactionModel(),
    rating = rating,
    review = review,
    image = image?:"",
    name = name
)

fun ItemTransactionModel.asItemTransactionNetwork() = ItemTransactionNetwork(
    productId = productId,
    variantName = variantName,
    quantity = quantity
)

fun List<ItemTransactionModel>.asItemTransactionNetwork(): List<ItemTransactionNetwork> =
    this.map { it.asItemTransactionNetwork() }

fun ItemTransactionNetwork.asItemTransactionModel() = ItemTransactionModel(
    productId = productId,
    variantName = variantName,
    quantity = quantity
)

fun List<ItemTransactionNetwork>.asItemTransactionModel(): List<ItemTransactionModel> =
    this.map { it.asItemTransactionModel() }
