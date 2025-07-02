package com.example.ecommerceapp.screen.wishlist

import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.WishlistModel
import com.example.ecommerceapp.firebase.WishlistAnalytics
import com.example.core.domain.usecase.AddToCartUseCase
import com.example.core.domain.usecase.GetWishlistUseCase
import com.example.core.domain.usecase.RemoveFromWishlistUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class WishlistViewModelTest {

    private lateinit var getWishlistUseCase: GetWishlistUseCase
    private lateinit var  removeFromWishlistUseCase: RemoveFromWishlistUseCase
    private lateinit var  addToCartUseCase: AddToCartUseCase
    private lateinit var  wishlistAnalytics: WishlistAnalytics

    private lateinit var viewModel: WishlistViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        getWishlistUseCase = mockk()
        removeFromWishlistUseCase = mockk()
        addToCartUseCase = mockk()
        wishlistAnalytics = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getWishlist - success updates UI state`() = runTest {
        val fakeResponse = listOf(
            WishlistModel(
                productId = "1",
                productName = "product",
                productImage = "image",
                productRating = 4.4f,
                unitPrice = 15000000,
                variantName = "RAM 16GB",
                stock = 4,
                store = "store",
                sale = 5,
            )
        )
        val flow = flowOf(EcommerceResponse.Success(fakeResponse))
        coEvery { getWishlistUseCase.invoke() } returns flow
        coEvery { wishlistAnalytics.trackViewWishlist(any()) } just Runs

        viewModel = WishlistViewModel(getWishlistUseCase,removeFromWishlistUseCase,addToCartUseCase,wishlistAnalytics)

        viewModel.getWishlist()
        advanceUntilIdle()

        viewModel.uiState.test{
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isError)
            assertEquals(1, state.wishlists.size)
        }

        coVerify { wishlistAnalytics.trackViewWishlist(any())}
    }

    @Test
    fun `getWishlist - failure sets isError true`() = runTest {
        val flow = flowOf(EcommerceResponse.Failure(400,"error"))
        coEvery { getWishlistUseCase.invoke() } returns flow
        coEvery { wishlistAnalytics.trackWishlistFailed("error") } just Runs

        viewModel = WishlistViewModel(getWishlistUseCase,removeFromWishlistUseCase,addToCartUseCase,wishlistAnalytics)

        viewModel.getWishlist()
        advanceUntilIdle()

        viewModel.uiState.test{
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isError)
        }

        coVerify { wishlistAnalytics.trackWishlistFailed("error") }
    }

    @Test
    fun `deleteWishlist removes item and emits event`() = runTest {
        val fakeResponse = listOf(
            WishlistModel(
                productId = "1",
                productName = "product",
                productImage = "image",
                productRating = 4.4f,
                unitPrice = 15000000,
                variantName = "RAM 16GB",
                stock = 4,
                store = "store",
                sale = 5,
            )
        )

        val flow = flowOf(EcommerceResponse.Success(fakeResponse))
        coEvery { getWishlistUseCase.invoke() } returns flow
        coEvery { removeFromWishlistUseCase.invoke("1") } just Runs
        every { wishlistAnalytics.trackDeleteWishlistButtonClicked("1") } just Runs

        viewModel = WishlistViewModel(
            getWishlistUseCase, removeFromWishlistUseCase, addToCartUseCase, wishlistAnalytics
        )

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(WishlistEvent.ShowSnackbar("Success Remove from WishList"), awaitItem())
                cancel() // end collection after receiving the event
            }
        }

        viewModel.deleteWishlist("1")
        advanceUntilIdle()
        job.join()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(0, state.wishlists.size)
            cancelAndIgnoreRemainingEvents()
        }

        coVerify { removeFromWishlistUseCase.invoke("1") }
        coVerify { wishlistAnalytics.trackDeleteWishlistButtonClicked("1") }
    }

    @Test
    fun `addToCart emits snackbar and triggers use case`() = runTest {
        val wishlistModel = WishlistModel(
            productId = "1",
            productName = "product",
            productImage = "image",
            productRating = 4.4f,
            unitPrice = 15000000,
            variantName = "RAM 16GB",
            stock = 4,
            store = "store",
            sale = 5,
        )
        val fakeResponse = listOf(wishlistModel)

        val flow = flowOf(EcommerceResponse.Success(fakeResponse))
        coEvery { getWishlistUseCase.invoke() } returns flow
        coEvery { addToCartUseCase.invoke(any()) } just Runs
        every { wishlistAnalytics.trackAddCartButtonClicked() } just Runs

        viewModel = WishlistViewModel(
            getWishlistUseCase, removeFromWishlistUseCase, addToCartUseCase, wishlistAnalytics
        )

        viewModel.getWishlist()
        advanceUntilIdle()

        // ✅ Start collecting before triggering the event
        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(WishlistEvent.ShowSnackbar("Success Add to Cart"), awaitItem())
                cancel() // end collection after receiving the event
            }
        }

        viewModel.addToCart(wishlistModel.productId)
        advanceUntilIdle()
        job.join()

        coVerify { addToCartUseCase.invoke(any()) }
        coVerify { wishlistAnalytics.trackAddCartButtonClicked() }
    }

    @Test
    fun `setClickedGrid toggles UI state and tracks analytics`() = runTest {
        val wishlistModel = WishlistModel(
            productId = "1",
            productName = "product",
            productImage = "image",
            productRating = 4.4f,
            unitPrice = 15000000,
            variantName = "RAM 16GB",
            stock = 4,
            store = "store",
            sale = 5,
        )
        val fakeResponse = listOf(wishlistModel)
        val flow = flowOf(EcommerceResponse.Success(fakeResponse))
        coEvery { getWishlistUseCase.invoke() } returns flow
        every { wishlistAnalytics.trackGridView(any()) } just Runs

        viewModel = WishlistViewModel(
            getWishlistUseCase,
            removeFromWishlistUseCase,
            addToCartUseCase,
            wishlistAnalytics
        )

        // Act - trigger the toggle
        viewModel.setClickedGrid()
        advanceUntilIdle()

        // Assert - value toggled
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue (state.isClickedGrid)
        }

        // Assert - analytics tracked
        coVerify { wishlistAnalytics.trackGridView(true) }
    }
}
