package com.example.core.data.data.mapper

import com.example.core.data.mapper.asFulfillmentModel
import com.example.core.data.network.model.FulfillmentNetwork

import kotlin.test.Test
import kotlin.test.assertEquals

class FulfillmentMapperTest {

    @Test
    fun `should map non-null FulfillmentNetwork to FulfillmentModel correctly`() {
        val network = FulfillmentNetwork(
            invoiceId = "INV123",
            status = true,
            date = "2025-06-15",
            time = "14:00",
            payment = "Transfer Bank",
            total = 100000
        )

        val model = network.asFulfillmentModel()

        assertEquals("INV123", model.invoiceId)
        assertEquals(true, model.status)
        assertEquals("2025-06-15", model.date)
        assertEquals("14:00", model.time)
        assertEquals("Transfer Bank", model.payment)
        assertEquals(100000, model.total)
    }

    @Test
    fun `should map null values in FulfillmentNetwork to default values in FulfillmentModel`() {
        val network = FulfillmentNetwork(
            invoiceId = null,
            status = null,
            date = null,
            time = null,
            payment = null,
            total = null
        )

        val model = network.asFulfillmentModel()

        assertEquals("", model.invoiceId)
        assertEquals(false, model.status)
        assertEquals("", model.date)
        assertEquals("", model.time)
        assertEquals("", model.payment)
        assertEquals(0, model.total)
    }
}
