package com.example.ecommerceapp.screen.shared

import app.cash.turbine.test
import com.example.core.ui.model.Cart
import com.example.core.ui.model.Fulfillment
import com.example.core.ui.model.ProductDetail
import com.example.core.ui.model.ProductVariant
import com.example.core.ui.model.Transaction
import com.example.core.ui.mapper.asCart
import com.example.core.ui.mapper.asFulfillment
import com.example.ecommerceapp.firebase.CartAnalytics
import com.example.core.domain.usecase.UpdateCartQuantityUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {

    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase = mockk(relaxed = true)
    private val cartAnalytics: CartAnalytics = mockk(relaxed = true)
    private lateinit var viewModel: SharedViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = SharedViewModel(updateCartQuantityUseCase, cartAnalytics)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setCheckedCartItems updates state and triggers analytics`() = runTest {
        val fakeCart = listOf(Cart(
            productId = "1",
            productName = "cart1",
            productImage = "image1",
            variantName = "variant1",
            quantity = 1,
            unitPrice = 100,
            stock = 5,
            isCheck = false
        ))
        coEvery { cartAnalytics.trackCheckedCartItems(fakeCart) } just Runs
        viewModel.setCheckedCartItems(fakeCart)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(fakeCart, state.checkedCarts)
        }

        coVerify { cartAnalytics.trackCheckedCartItems(fakeCart) }
    }

    @Test
    fun `clearCheckedCarts should empty the checked cart list`() = runTest {
        val fakeCart = listOf(Cart(
            productId = "1",
            productName = "cart1",
            productImage = "image1",
            variantName = "variant1",
            quantity = 1,
            unitPrice = 100,
            stock = 5,
            isCheck = false
        ))
        viewModel.setCheckedCartItems(fakeCart)
        viewModel.clearCheckedCarts()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(0, state.checkedCarts.size)
        }
    }

    @Test
    fun `setDetailCartItems maps to cart and updates state`() = runTest {
        val testProductId = "1"
        val testVariant = ProductVariant(variantName = "variant", variantPrice = 200)
        val testProductDetail = ProductDetail(
            productId = testProductId, productName = "Test", productPrice = 1000, image = listOf("image"), brand = "brand",description="desc",store="store",
            sale =0 , stock = 0, totalRating = 0, totalReview = 10, totalSatisfaction = 2, productRating = 2.4F,
            productVariant = listOf(testVariant), isWishlist = false
        )
        val expectedCart = testProductDetail.asCart(testVariant)
        coEvery { cartAnalytics.trackDetailCartItemSelected(testProductDetail, testVariant) } just Runs
        viewModel.setDetailCartItems(testVariant, testProductDetail)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(listOf(expectedCart), state.checkedCarts)
        }

        coVerify { cartAnalytics.trackDetailCartItemSelected(testProductDetail, testVariant) }
    }

    @Test
    fun `updateQuantity increments if under stock limit`() = runTest {
        val cart = Cart(
            productId = "1",
            productName = "cart1",
            productImage = "image1",
            variantName = "variant1",
            quantity = 1,
            unitPrice = 100,
            stock = 5,
            isCheck = false
        )
        coEvery { updateCartQuantityUseCase("1", 2) } just Runs
        viewModel.setCheckedCartItems(listOf(cart))
        viewModel.updateQuantity("1", isIncrement = true)

        viewModel.uiState.test {
            val state = awaitItem()
            val updated = state.checkedCarts.find { it.productId == "1" }
            assertEquals(2, updated?.quantity)
        }

        coVerify { updateCartQuantityUseCase("1", 2) }
    }

    @Test
    fun `updateQuantity does not decrement below 1`() = runTest {
        val cart = Cart(
            productId = "1",
            productName = "cart1",
            productImage = "image1",
            variantName = "variant1",
            quantity = 1,
            unitPrice = 100,
            stock = 5,
            isCheck = false
        )
        coEvery { updateCartQuantityUseCase("1", 1) } just Runs
        viewModel.setCheckedCartItems(listOf(cart))
        viewModel.updateQuantity("1", isIncrement = false)
        viewModel.updateQuantity("1", isIncrement = false)

        // Quantity should stay the same (1), and no API call should be made
        viewModel.uiState.test {
            val state = awaitItem()
            val updated = state.checkedCarts.find { it.productId == "1" }
            assertEquals(1, updated?.quantity)
        }

        coVerify(exactly = 0) { updateCartQuantityUseCase(any(), any()) }
    }

    @Test
    fun `setTransaction should map transaction to fulfillment`() = runTest {
        val transaction = Transaction(
            invoiceId = "1",
            name = "Product",
            total = 15000000,
            payment = "BCA",
            status = true,
            date = "date",
            time = "time",
            items = listOf(),
            rating = 4,
            review = "review",
            image = "https://image.url",
        )
        val expectedFulfillment = transaction.asFulfillment()

        viewModel.setTransaction(transaction)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(expectedFulfillment, state.fulfillment)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setFulfillment should update the fulfillment state`() = runTest {
        val fulfillment = Fulfillment("id", true, "name", "address", "note", 100)
        viewModel.setFulfillment(fulfillment)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(fulfillment, state.fulfillment)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `clearFulfillment resets the fulfillment state`() = runTest {
        val fulfillment = Fulfillment("id", true, "name", "address", "note", 100)
        viewModel.setFulfillment(fulfillment)
        viewModel.clearFulfillment()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(Fulfillment("", false, "", "", "", 0), state.fulfillment)
        }
    }

}