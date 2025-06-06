package com.example.ecommerceapp.data.ui.mapper

import com.example.ecommerceapp.data.domain.ItemTransactionModel
import com.example.ecommerceapp.data.domain.TransactionModel
import com.example.ecommerceapp.data.ui.Fulfillment
import com.example.ecommerceapp.data.ui.ItemTransaction
import com.example.ecommerceapp.data.ui.Transaction

fun Transaction.asFulfillment() = Fulfillment(
    invoiceId = invoiceId,
    status = status,
    date = date,
    time = time,
    payment = payment,
    total = total
)
fun TransactionModel.asTransaction() = Transaction(
    invoiceId = invoiceId,
    status = status,
    date = date,
    time = time,
    payment = payment,
    total = total,
    items = items.asItemTransaction(),
    rating = rating?:0,
    review = review?:"",
    name = name,
    image = image
)

fun List<ItemTransactionModel>.asItemTransaction() = map { it.asItemTransaction() }

fun ItemTransactionModel.asItemTransaction()= ItemTransaction(
    productId = productId,
    variantName = variantName,
    quantity = quantity
)