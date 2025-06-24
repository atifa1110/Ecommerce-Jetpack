package com.example.core.data.data.mapper

import com.example.core.data.mapper.asPaymentItemModel
import com.example.core.data.mapper.asPaymentModel
import com.example.core.data.network.model.PaymentNetwork
import kotlin.test.Test
import kotlin.test.assertEquals


class PaymentMapperTest {

    @Test
    fun `should map PaymentNetworkItem to PaymentModelItem correctly`() {
        val networkItem = PaymentNetwork.PaymentNetworkItem(
            label = "Bank BCA",
            image = "https://example.com/bca.png",
            status = true
        )

        val modelItem = networkItem.asPaymentItemModel()

        assertEquals(networkItem.label, modelItem.label)
        assertEquals(networkItem.image, modelItem.image)
        assertEquals(networkItem.status, modelItem.status)
    }

    @Test
    fun `should map PaymentNetwork to PaymentModel correctly`() {
        val network = PaymentNetwork(
            title = "Transfer Bank",
            item = listOf(
                PaymentNetwork.PaymentNetworkItem("BCA", "bca.png", true),
                PaymentNetwork.PaymentNetworkItem("Mandiri", "mandiri.png", false)
            )
        )

        val model = network.asPaymentModel()

        assertEquals(network.title, model.title)
        assertEquals(2, model.item.size)
        assertEquals("BCA", model.item[0].label)
        assertEquals("mandiri.png", model.item[1].image)
        assertEquals(false, model.item[1].status)
    }

    @Test
    fun `should map List of PaymentNetwork to List of PaymentModel correctly`() {
        val list = listOf(
            PaymentNetwork(
                title = "Transfer Bank",
                item = listOf(
                    PaymentNetwork.PaymentNetworkItem("BCA", "bca.png", true)
                )
            ),
            PaymentNetwork(
                title = "E-Wallet",
                item = listOf(
                    PaymentNetwork.PaymentNetworkItem("OVO", "ovo.png", true),
                    PaymentNetwork.PaymentNetworkItem("GoPay", "gopay.png", true)
                )
            )
        )

        val result = list.asPaymentModel()

        assertEquals(2, result.size)
        assertEquals("Transfer Bank", result[0].title)
        assertEquals("OVO", result[1].item[0].label)
        assertEquals("gopay.png", result[1].item[1].image)
    }
}
