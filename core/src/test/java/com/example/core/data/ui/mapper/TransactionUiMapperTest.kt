package com.example.core.data.ui.mapper

import com.example.core.ui.mapper.asFulfillment
import com.example.core.ui.mapper.asItemTransaction
import com.example.core.ui.mapper.asTransaction
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.domain.model.TransactionModel
import com.example.core.ui.model.Transaction
import junit.framework.TestCase.assertEquals
import kotlin.test.Test

class TransactionUiMapperTest {
    @Test
    fun `TransactionModel should map to Transaction correctly`() {
        val model = TransactionModel(
            invoiceId = "INV-001",
            status = true,
            date = "2024-06-16",
            time = "12:00",
            payment = "Gopay",
            total = 150000,
            items = listOf(
                ItemTransactionModel("P001", "Size M", 2)
            ),
            rating = 5,
            review = "Bagus banget",
            name = "Atifa",
            image = "img.jpg"
        )

        val transaction = model.asTransaction()

        assertEquals(model.invoiceId, transaction.invoiceId)
        assertEquals(model.status, transaction.status)
        assertEquals(model.date, transaction.date)
        assertEquals(model.time, transaction.time)
        assertEquals(model.payment, transaction.payment)
        assertEquals(model.total, transaction.total)
        assertEquals(model.rating, transaction.rating)
        assertEquals(model.review, transaction.review)
        assertEquals(model.name, transaction.name)
        assertEquals(model.image, transaction.image)

        assertEquals(1, transaction.items.size)
        assertEquals("P001", transaction.items[0].productId)
        assertEquals("Size M", transaction.items[0].variantName)
        assertEquals(2, transaction.items[0].quantity)
    }

    @Test
    fun `Transaction should map to Fulfillment correctly`() {
        val transaction = Transaction(
            invoiceId = "INV-001",
            status = true,
            date = "2024-06-16",
            time = "12:00",
            payment = "Gopay",
            total = 150000,
            items = emptyList(),
            rating = 5,
            review = "Bagus",
            name = "Atifa",
            image = "img.jpg"
        )

        val fulfillment = transaction.asFulfillment()

        assertEquals(transaction.invoiceId, fulfillment.invoiceId)
        assertEquals(transaction.status, fulfillment.status)
        assertEquals(transaction.date, fulfillment.date)
        assertEquals(transaction.time, fulfillment.time)
        assertEquals(transaction.payment, fulfillment.payment)
        assertEquals(transaction.total, fulfillment.total)
    }

    @Test
    fun `ItemTransactionModel should map to ItemTransaction correctly`() {
        val model = ItemTransactionModel(
            productId = "P001",
            variantName = "Size M",
            quantity = 2
        )

        val item = model.asItemTransaction()

        assertEquals(model.productId, item.productId)
        assertEquals(model.variantName, item.variantName)
        assertEquals(model.quantity, item.quantity)
    }

    @Test
    fun `List of ItemTransactionModel should map to List of ItemTransaction correctly`() {
        val models = listOf(
            ItemTransactionModel("P001", "Size M", 2),
            ItemTransactionModel("P002", "Size L", 1)
        )

        val result = models.asItemTransaction()

        assertEquals(2, result.size)
        assertEquals("P001", result[0].productId)
        assertEquals("Size L", result[1].variantName)
    }
}