package com.example.core.data.ui.mapper

import com.example.core.ui.mapper.asFulfillment
import com.example.core.domain.model.FulfillmentModel
import junit.framework.TestCase.assertEquals
import kotlin.test.Test


class FulfillmentUiMapperTest {
    @Test
    fun `FulfillmentModel should map to Fulfillment correctly`() {
        val model = FulfillmentModel(
            invoiceId = "INV123",
            status = true,
            date = "2025-06-16",
            time = "10:00",
            payment = "Credit Card",
            total = 250000
        )

        val fulfillment = model.asFulfillment()

        assertEquals(model.invoiceId, fulfillment.invoiceId)
        assertEquals(model.status, fulfillment.status)
        assertEquals(model.date, fulfillment.date)
        assertEquals(model.time, fulfillment.time)
        assertEquals(model.payment, fulfillment.payment)
        assertEquals(model.total, fulfillment.total)
    }
}