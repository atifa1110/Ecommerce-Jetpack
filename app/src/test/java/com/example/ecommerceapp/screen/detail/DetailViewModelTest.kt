package com.example.ecommerceapp.screen.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.core.domain.model.ProductDetailModel
import com.example.core.domain.model.ProductVariantModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.mapper.asCartModel
import com.example.core.ui.mapper.asProductDetail
import com.example.core.ui.mapper.asProductVariant
import com.example.core.ui.mapper.asWishlistModel
import com.example.ecommerceapp.firebase.ProductAnalyticsManager
import com.example.ecommerceapp.graph.DetailsDestination
import com.example.core.domain.usecase.AddToCartUseCase
import com.example.core.domain.usecase.AddToWishlistUseCase
import com.example.core.domain.usecase.GetDetailProductUseCase
import com.example.core.domain.usecase.RemoveFromWishlistUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
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
class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    private val getDetailProductUseCase: GetDetailProductUseCase = mockk()
    private val addToWishlistUseCase: AddToWishlistUseCase = mockk(relaxed = true)
    private val removeFromWishlistUseCase: RemoveFromWishlistUseCase = mockk(relaxed = true)
    private val addToCartUseCase: AddToCartUseCase = mockk(relaxed = true)
    private val analyticsManager: ProductAnalyticsManager = mockk(relaxed = true)

    private val testProductId = "test1"
    private val testVariant = ProductVariantModel(variantName = "variant", variantPrice = 200)
    private val testVariant1 = ProductVariantModel(variantName = "variant1", variantPrice = 400)
    private val testProductDetail = ProductDetailModel(
        productId = testProductId, productName = "Test", productPrice = 1000, image = listOf("image"), brand = "brand",description="desc",store="store",
        sale =0 , stock = 0, totalRating = 0, totalReview = 10, totalSatisfaction = 2, productRating = 2.4F,
        productVariant = listOf(testVariant,testVariant1), isWishlist = false
    )

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        val savedStateHandle = SavedStateHandle(mapOf("id" to testProductId))

        // 👇 Mock the object and method
        mockkObject(DetailsDestination)
        every { DetailsDestination.fromSavedStateHandle(any()) } returns testProductId

        viewModel = DetailViewModel(
            getDetailProductUseCase,
            addToWishlistUseCase,
            removeFromWishlistUseCase,
            addToCartUseCase,
            analyticsManager,
            savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadDetailProduct emits Success and updates state`() = runTest {
        val flow = flow{
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Success(testProductDetail))
        }

        coEvery { getDetailProductUseCase.invoke(testProductId) } returns flow
        coEvery { analyticsManager.trackViewItemDetail(any()) } just Runs

        viewModel.loadDetailProduct()
        advanceTimeBy(50)

        viewModel.uiState.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)
            assertFalse(loading.isError)

            val state = awaitItem()
            assertFalse(state.isError)
            assertFalse(state.isLoading)
            assertEquals(testProductDetail.asProductDetail().productId, state.productDetail.productId)
        }

        coVerify { analyticsManager.trackViewItemDetail(any()) }
    }

    @Test
    fun `loadDetailProduct emits failure and updates state`() = runTest {
        val exception = "Network error"
        val flow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Failure(400, exception))
        }

        coEvery { getDetailProductUseCase.invoke(testProductId) } returns flow

        val job = launch {
            viewModel.eventFlow.test {
                TestCase.assertEquals(
                    DetailEvent.ShowSnackbar(exception),
                    awaitItem()
                )
                cancel()
            }
        }

        viewModel.loadDetailProduct()
        advanceTimeBy(50)

        viewModel.uiState.test {
            val loading = awaitItem()
            assertTrue(loading.isLoading)
            assertFalse(loading.isError)

            val state = awaitItem()
            assertTrue(state.isError)
            assertFalse(state.isLoading)
        }

        job.join()
    }

    @Test
    fun `onVariantSelected should update selectedVariant and totalPrice`() = runTest {
        val flow = flowOf(EcommerceResponse.Success(testProductDetail))
        coEvery { getDetailProductUseCase.invoke(testProductId) } returns flow

        // First, load the product detail so productPrice is set
        viewModel.loadDetailProduct()
        advanceTimeBy(50)

        // Then, select a variant
        val variant = testVariant1.asProductVariant()
        viewModel.onVariantSelected(variant)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1400, state.totalPrice)
        }
    }

    @Test
    fun `onAddToCart should call use case and emit snackbar`() = runTest {
        val detail = testProductDetail.asProductDetail()
        val variant = testVariant.asProductVariant()
        val flow = flowOf(EcommerceResponse.Success(testProductDetail))
        coEvery { getDetailProductUseCase.invoke(testProductId) } returns flow
        coEvery { addToCartUseCase.invoke(detail.asCartModel(variant)) } just Runs

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    DetailEvent.ShowSnackbar("Success Add to Cart"),
                    awaitItem()
                )
                cancel()
            }
        }

        // First, load the product detail so productPrice is set
        viewModel.loadDetailProduct()
        viewModel.onAddToCart()
        advanceUntilIdle()
        job.join()

        coVerify { addToCartUseCase.invoke(any()) }
    }

    @Test
    fun `onWishlistDetail toggles wishlist and emits add snackbar`() = runTest {
        val wishlist = testProductDetail.asProductDetail().asWishlistModel(1200,"variant")
        val flow = flowOf(EcommerceResponse.Success(testProductDetail.copy(isWishlist = false)))
        coEvery { getDetailProductUseCase.invoke(testProductId) } returns flow
        coEvery { addToWishlistUseCase.invoke(wishlist) } just Runs

        val job = launch {
            viewModel.eventFlow.test {
                assertEquals(
                    DetailEvent.ShowSnackbar("Success Add to WishList"),
                    awaitItem()
                )
                cancel()
            }
        }

        viewModel.loadDetailProduct()
        advanceUntilIdle()

        viewModel.onWishlistDetail()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.productDetail.isWishlist)
            assertEquals(1000, state.productDetail.productPrice)
        }
        job.join()

        coVerify { addToWishlistUseCase.invoke(wishlist) }
    }

    @Test
    fun `onWishlistDetail toggles wishlist and emits remove snackbar`() = runTest {
        val wishlist = testProductDetail.asProductDetail().asWishlistModel(1200,"variant")
        val flow = flowOf(EcommerceResponse.Success(testProductDetail.copy(isWishlist = false)))
        coEvery { getDetailProductUseCase.invoke(testProductId) } returns flow

        // Step 2: Mock add and remove use cases
        coEvery { addToWishlistUseCase.invoke(wishlist) } just Runs
        coEvery { removeFromWishlistUseCase.invoke(wishlist.productId) } just Runs

        val job = launch {
            viewModel.eventFlow.test {
                // 1st toggle: Add
                assertEquals(DetailEvent.ShowSnackbar("Success Add to WishList"), awaitItem())
                // 2nd toggle: Remove
                assertEquals(DetailEvent.ShowSnackbar("Success Remove from WishList"), awaitItem())
                cancel()
            }
        }

        viewModel.loadDetailProduct()
        advanceUntilIdle()

        viewModel.onWishlistDetail()
        advanceUntilIdle()

        viewModel.onWishlistDetail()
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.productDetail.isWishlist)
            assertEquals(1000, state.productDetail.productPrice)
        }
        job.join()

        coVerify { addToWishlistUseCase.invoke(wishlist) }
        coVerify { removeFromWishlistUseCase.invoke(wishlist.productId) }
    }

    @Test
    fun `checkoutAnalytics should trigger product analytics tracking`() {
        // Arrange
        coEvery { analyticsManager.trackDetailAddToCheckoutButtonClicked() } just Runs

        // Act
        viewModel.checkoutAnalytics()

        // Assert
        coVerify { analyticsManager.trackDetailAddToCheckoutButtonClicked() }

    }

}