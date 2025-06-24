package com.example.core.ui.mapper

import com.example.core.domain.model.ItemTransactionModel
import com.example.core.domain.model.TransactionModel
import com.example.core.ui.model.Fulfillment
import com.example.core.ui.model.ItemTransaction
import com.example.core.ui.model.Transaction

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