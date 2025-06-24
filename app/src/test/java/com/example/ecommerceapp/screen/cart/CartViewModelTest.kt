package com.example.ecommerceapp.screen.cart

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.CartModel
import com.example.core.ui.mapper.asCart
import com.example.ecommerceapp.firebase.CartAnalytics
import com.example.core.domain.usecase.DeleteCartUseCase
import com.example.core.domain.usecase.GetCartUseCase
import com.example.core.domain.usecase.UpdateCartQuantityUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.collections.first
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CartViewModelTest {

    private lateinit var viewModel: CartViewModel
    private lateinit var updateCartQuantityUseCase: UpdateCartQuantityUseCase
    private lateinit var deleteCartUseCase: DeleteCartUseCase
    private lateinit var getCartUseCase: GetCartUseCase
    private lateinit var cartAnalytics: CartAnalytics

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        updateCartQuantityUseCase =  mockk(relaxed = true)
        deleteCartUseCase = mockk(relaxed = true)
        getCartUseCase = mockk()
        cartAnalytics = mockk(relaxed = true)
        viewModel = CartViewModel(
            getCartUseCase,
            updateCartQuantityUseCase,
            deleteCartUseCase,cartAnalytics
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val fakeCart = CartModel(
        productId = "1",
        productName = "cart1",
        image = "image1",
        variantName = "variant1",
        quantity = 1,
        unitPrice = 100,
        stock = 5,
    )

    private val fakeCart1 = CartModel(
        productId = "2",
        productName = "cart2",
        image = "image2",
        variantName = "variant2",
        quantity = 3,
        unitPrice = 100,
        stock = 5,
    )

    private val listCarts = listOf(fakeCart,fakeCart1)

    @Test
    fun `isAllChecked should return false if all item is unchecked`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.isAllChecked.test {
            val state = awaitItem()
            assertFalse(state)
        }
    }

    @Test
    fun `isAllChecked should return true if all item is checked`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.isAllChecked.test {
            // First: initially should be false
            assertFalse(awaitItem())

            // Trigger update
            viewModel.setAllChecked(true)
            advanceUntilIdle()

            // Then: should become true
            assertTrue(awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isAnyItemChecked should return false if no item is checked`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.isAnyItemChecked.test {
            // First: initially should be false
            assertFalse(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `isAnyItemChecked should return true if at least one item is checked`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.isAnyItemChecked.test {
            // First: initially should be false
            assertFalse(awaitItem())

            // Trigger update
            viewModel.toggleItemChecked("1")
            advanceUntilIdle()

            // Then: should become true
            assertTrue(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `checkedCarts should return only items with isCheck true`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.checkedCarts.test {
            // Initial state: empty (no item is checked)
            assertTrue(awaitItem().isEmpty())

            // Trigger update
            viewModel.toggleItemChecked("1")
            val checked = awaitItem()

            assertEquals(1, checked.size)
            assertEquals("1", checked.first().productId)
            assertTrue(checked.first().isCheck)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `totalCheckedPrice should return correct total`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.totalCheckedPrice.test {
            assertTrue(awaitItem() == 0)

            // Trigger update
            viewModel.toggleItemChecked("1")
            val checked = awaitItem()

            val expected = 100
            assertEquals(expected, checked)
        }
    }

    @Test
    fun `loadCartItems emits loading then success and triggers analytics`() = runTest {
        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Success(listCarts))
        }

        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { cartAnalytics.trackViewCart(any()) } just Runs

        viewModel.loadCartItems()
        advanceTimeBy(50)

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertFalse(successState.isError)
            assertEquals(2, successState.carts.size)

            cancelAndIgnoreRemainingEvents()
        }

        coVerify { cartAnalytics.trackViewCart(listCarts.map { it.asCart() }) }
    }

    @Test
    fun `loadCartItems emits loading then failure and triggers failure analytics`() = runTest {
        val exception = "Network error"
        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Failure(400, exception))
        }

        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { cartAnalytics.trackViewCartFailed(exception) } just Runs

        viewModel.loadCartItems()
        advanceTimeBy(50)

        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertTrue(loadingState.isLoading)
            assertFalse(loadingState.isError)

            val errorState = awaitItem()
            assertFalse(errorState.isLoading)
            assertTrue(errorState.isError)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { cartAnalytics.trackViewCartFailed(exception) }
    }

    @Test
    fun `increaseQuantity updates quantity and triggers use case and analytics`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { updateCartQuantityUseCase.invoke("1",2) } just Runs
        coEvery { cartAnalytics.trackCartQuantityChanged("1", "cart1", "variant1", 2, "increase") } just Runs

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.updateQuantity("1",true)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2,  state.carts.first { it.productId == "1" }.quantity)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { updateCartQuantityUseCase("1", 2) }
        coVerify { cartAnalytics.trackCartQuantityChanged("1", "cart1", "variant1", 2, "increase") }
    }

    @Test
    fun `increaseQuantity does not exceed stock`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { updateCartQuantityUseCase.invoke("1",6) } just Runs
        coEvery { cartAnalytics.trackCartQuantityChanged("1", "cart1", "variant1", 5, "increase") } just Runs

        viewModel.loadCartItems()
        advanceUntilIdle()

        repeat(6) {
            viewModel.updateQuantity("1",true)
        }
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(5, state.carts.first { it.productId == "1" }.quantity)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1) { updateCartQuantityUseCase("1", 2) }
        coVerify(exactly = 1) { updateCartQuantityUseCase("1", 3) }
        coVerify(exactly = 1) { updateCartQuantityUseCase("1", 4) }
        coVerify(exactly = 1) { updateCartQuantityUseCase("1", 5) }
        coVerify(exactly = 0) { updateCartQuantityUseCase("1", 6) }

        coVerify { cartAnalytics.trackCartQuantityChanged("1", "cart1", "variant1", 5, "increase") }
    }

    @Test
    fun `decreaseQuantity updates quantity and triggers use case and analytics`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { updateCartQuantityUseCase.invoke("2",2) } just Runs
        coEvery { cartAnalytics.trackCartQuantityChanged("2", "cart2", "variant2", 2, "decrease") } just Runs

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.updateQuantity("2",false)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2,  state.carts.first { it.productId == "2" }.quantity)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { updateCartQuantityUseCase("2", 2) }
        coVerify { cartAnalytics.trackCartQuantityChanged("2", "cart2", "variant2", 2, "decrease") }
    }

    @Test
    fun `decreaseQuantity does not go below 1`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { updateCartQuantityUseCase.invoke("2",3) } just Runs
        coEvery { cartAnalytics.trackCartQuantityChanged("2", "cart2", "variant2", 1, "decrease") } just Runs

        viewModel.loadCartItems()
        advanceUntilIdle()

        repeat(3) {
            viewModel.updateQuantity("2", false)
        }
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1,  state.carts.first { it.productId == "2" }.quantity)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify(exactly = 1){ updateCartQuantityUseCase("2", 2) }
        coVerify(exactly = 1){ updateCartQuantityUseCase("2", 1) }
        coVerify(exactly = 0){ updateCartQuantityUseCase("2", 0) }
        coVerify { cartAnalytics.trackCartQuantityChanged("2", "cart2", "variant2", 1, "decrease") }
    }

    @Test
    fun `deleteCartItem removes item from state and triggers analytics`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { deleteCartUseCase("1") } just Runs
        coEvery { cartAnalytics.trackRemoveFromCart("1") } just Runs

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.deleteCartItem("1")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.carts.size)
            assertEquals("2", state.carts.first { it.productId == "2" }.productId)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { deleteCartUseCase("1") }
        coVerify { cartAnalytics.trackRemoveFromCart("1") }
    }

    @Test
    fun `deleteCheckedItems removes all checked items and calls delete use case`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { deleteCartUseCase("1") } just Runs

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.toggleItemChecked("1")
        advanceUntilIdle()

        viewModel.deleteCheckedItems()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.carts.size)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { deleteCartUseCase("1") }
    }

    @Test
    fun `toggleItemChecked toggles item check state and triggers analytics`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow
        coEvery { cartAnalytics.trackBuyButtonClicked(any()) } just Runs

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.toggleItemChecked("1")
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue( state.carts.first { it.productId == "1" }.isCheck)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { cartAnalytics.trackBuyButtonClicked(any()) }
    }

    @Test
    fun `clearSelection unchecks all cart items`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.setAllChecked(true)
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.carts.all { it.isCheck })

        viewModel.setAllChecked(false)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.carts.all { !it.isCheck })
            cancelAndIgnoreRemainingEvents()
        }

    }

    @Test
    fun `setSelection checks all cart items`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(listCarts))
        coEvery { getCartUseCase.invoke() } returns flow

        viewModel.loadCartItems()
        advanceUntilIdle()

        viewModel.setAllChecked(true)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.carts.all { it.isCheck })
            cancelAndIgnoreRemainingEvents()
        }

    }


}