package com.example.core.data.ui.mapper

import com.example.core.ui.mapper.asPayment
import com.example.core.ui.mapper.asPaymentItem
import com.example.core.domain.model.PaymentModel
import kotlin.test.Test
import kotlin.test.assertEquals

class PaymentUiMapperTest {

    @Test
    fun `PaymentModelItem should map to PaymentItem correctly`() {
        val itemModel = PaymentModel.PaymentModelItem(
            label = "Gopay",
            image = "gopay.png",
            status = true
        )

        val item = itemModel.asPaymentItem()

        assertEquals(itemModel.label, item.label)
        assertEquals(itemModel.image, item.image)
        assertEquals(itemModel.status, item.status)
    }

    @Test
    fun `PaymentModel should map to Payment correctly`() {
        val model = PaymentModel(
            title = "E-Wallet",
            item = listOf(
                PaymentModel.PaymentModelItem("Gopay", "gopay.png", true),
                PaymentModel.PaymentModelItem("OVO", "ovo.png", false)
            )
        )

        val payment = model.asPayment()

        assertEquals(model.title, payment.title)
        assertEquals(2, payment.item.size)
        assertEquals("Gopay", payment.item[0].label)
        assertEquals("OVO", payment.item[1].label)
    }

    @Test
    fun `List of PaymentModel should map to List of Payment correctly`() {
        val models = listOf(
            PaymentModel("E-Wallet", listOf(PaymentModel.PaymentModelItem("Dana", "dana.png", true))),
            PaymentModel("Bank Transfer", listOf(PaymentModel.PaymentModelItem("BCA", "bca.png", true)))
        )

        val payments = models.asPayment()

        assertEquals(2, payments.size)
        assertEquals("E-Wallet", payments[0].title)
        assertEquals("Dana", payments[0].item[0].label)
        assertEquals("Bank Transfer", payments[1].title)
        assertEquals("BCA", payments[1].item[0].label)
    }

}