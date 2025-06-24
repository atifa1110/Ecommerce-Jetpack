package com.example.core.data.data.mapper

import com.example.core.data.mapper.asItemTransactionModel
import com.example.core.data.mapper.asItemTransactionNetwork
import com.example.core.data.mapper.asTransactionModel
import com.example.core.domain.model.ItemTransactionModel
import com.example.core.data.network.model.ItemTransactionNetwork
import com.example.core.data.network.model.TransactionNetwork
import kotlin.test.Test
import kotlin.test.assertEquals


class TransactionMapperTest {

    @Test
    fun `should map ItemTransactionNetwork to ItemTransactionModel correctly`() {
        val network = ItemTransactionNetwork(
            productId = "P001",
            variantName = "Size M",
            quantity = 2
        )

        val model = network.asItemTransactionModel()

        assertEquals("P001", model.productId)
        assertEquals("Size M", model.variantName)
        assertEquals(2, model.quantity)
    }

    @Test
    fun `should map list of ItemTransactionNetwork to list of ItemTransactionModel`() {
        val networkList = listOf(
            ItemTransactionNetwork("P001", "Size M", 2),
            ItemTransactionNetwork("P002", "Size L", 1)
        )

        val modelList = networkList.asItemTransactionModel()

        assertEquals(2, modelList.size)
        assertEquals("P002", modelList[1].productId)
        assertEquals("Size L", modelList[1].variantName)
        assertEquals(1, modelList[1].quantity)
    }

    @Test
    fun `should map ItemTransactionModel to ItemTransactionNetwork correctly`() {
        val model = ItemTransactionModel(
            productId = "P003",
            variantName = "Size XL",
            quantity = 3
        )

        val network = model.asItemTransactionNetwork()

        assertEquals("P003", network.productId)
        assertEquals("Size XL", network.variantName)
        assertEquals(3, network.quantity)
    }

    @Test
    fun `should map list of ItemTransactionModel to list of ItemTransactionNetwork`() {
        val modelList = listOf(
            ItemTransactionModel("P004", "Size S", 1),
            ItemTransactionModel("P005", "Size M", 2)
        )

        val networkList = modelList.asItemTransactionNetwork()

        assertEquals(2, networkList.size)
        assertEquals("P005", networkList[1].productId)
        assertEquals("Size M", networkList[1].variantName)
        assertEquals(2, networkList[1].quantity)
    }

    @Test
    fun `should map TransactionNetwork to TransactionModel correctly`() {
        val transactionNetwork = TransactionNetwork(
            invoiceId = "INV123",
            status = true,
            date = "2025-06-16",
            time = "14:00",
            payment = "OVO",
            total = 150000,
            items = listOf(
                ItemTransactionNetwork("P001", "Size M", 2)
            ),
            rating = 5,
            review = "Sangat puas!",
            image = null,
            name = "Adit"
        )

        val model = transactionNetwork.asTransactionModel()

        assertEquals("INV123", model.invoiceId)
        assertEquals(true, model.status)
        assertEquals("2025-06-16", model.date)
        assertEquals("14:00", model.time)
        assertEquals("OVO", model.payment)
        assertEquals(150000, model.total)
        assertEquals(1, model.items.size)
        assertEquals("P001", model.items[0].productId)
        assertEquals("Sangat puas!", model.review)
        assertEquals("", model.image) // default because nullable
        assertEquals("Adit", model.name)
    }
}
