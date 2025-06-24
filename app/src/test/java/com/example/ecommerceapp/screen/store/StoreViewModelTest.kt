package com.example.ecommerceapp.screen.store

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import app.cash.turbine.test
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.ui.model.Product
import com.example.ecommerceapp.firebase.ProductAnalyticsManager
import com.example.core.domain.usecase.GetProductFilterUseCase
import com.example.core.domain.usecase.GetSearchProductUseCase
import com.example.core.domain.usecase.RefreshTokenUseCase
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
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
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class StoreViewModelTest {

    private lateinit var getProductFilterUseCase: GetProductFilterUseCase
    private lateinit var getSearchProductUseCase: GetSearchProductUseCase
    private lateinit var refreshTokenUseCase: RefreshTokenUseCase
    private lateinit var productAnalyticsManager: ProductAnalyticsManager
    private lateinit var viewModel: StoreViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        getProductFilterUseCase = mockk()
        getSearchProductUseCase = mockk()
        refreshTokenUseCase = mockk()
        productAnalyticsManager = mockk(relaxed = true)

        viewModel = StoreViewModel(
            getProductFilterUseCase,
            getSearchProductUseCase,
            refreshTokenUseCase,
            productAnalyticsManager
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refreshToken emits loading and success`() = runTest {
        val testFlow = flow {
            emit(EcommerceResponse.Loading)
            delay(100)
            emit(EcommerceResponse.Success("Token refreshed"))
        }
        coEvery { refreshTokenUseCase.invoke() } returns testFlow

        var callbackCalled = false
        viewModel.refreshToken { callbackCalled = true }

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals("Token refreshed", state.userMessage)
            assertTrue(callbackCalled)
        }
    }

    @Test
    fun `refreshToken emits loading and failure`() = runTest {
        val error = "Network error"
        val testFlow = flowOf(
            EcommerceResponse.Loading,
            EcommerceResponse.Failure(400,error)
        )
        coEvery { refreshTokenUseCase.invoke() } returns testFlow

        var callbackCalled = false
        viewModel.refreshToken { callbackCalled = true }

        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(error, state.userMessage)
        }
    }

    @Test
    fun `onSuggestionSelected updates search and closes search`() = runTest {
        val suggestion = "lenovo"
        every { productAnalyticsManager.trackSearchSuggestionSelected(suggestion) } just Runs

        viewModel.onSuggestionSelected(suggestion)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(suggestion, state.productFilter.search)
            assertFalse(state.isSearchOpen)
            assertTrue(state.searchSuggestionState.suggestions.isEmpty())
        }
    }

    @Test
    fun `setSearchScreenOpen true updates state and tracks viewed`() = runTest {
        every { productAnalyticsManager.trackSearchScreenViewed() } just Runs

        viewModel.setSearchScreenOpen(true)

        val state = viewModel.uiState.value
        assertTrue(state.isSearchOpen)
        verify { productAnalyticsManager.trackSearchScreenViewed() }
    }

    @Test
    fun `setSearchScreenOpen false updates state and tracks closed`() = runTest {
        every { productAnalyticsManager.trackSearchScreenClosed() } just Runs

        viewModel.setSearchScreenOpen(false)

        val state = viewModel.uiState.value
        assertFalse(state.isSearchOpen)
        verify { productAnalyticsManager.trackSearchScreenClosed() }
    }

    @Test
    fun `setBottomSheetOpen true prepopulates bottom sheet fields`() = runTest {
        viewModel.setQuery("Nike", "100", "500", "price") // Pre-set filter values
        viewModel.setBottomSheetOpen(true)

        val state = viewModel.uiState.value
        assertTrue(state.isBottomSheetOpen)
        assertEquals("Nike", state.selectedBrand)
        assertEquals("price", state.selectedSort)
        assertEquals("100", state.lowestPrice)
        assertEquals("500", state.highestPrice)
    }

    @Test
    fun `setBottomSheetOpen false clears bottom sheet fields`() = runTest {
        // Set to true first
        viewModel.setBottomSheetOpen(true)
        // Then set to false
        viewModel.setBottomSheetOpen(false)

        val state = viewModel.uiState.value
        assertFalse(state.isBottomSheetOpen)
        assertNull(state.selectedBrand)
        assertNull(state.selectedSort)
        assertEquals("", state.lowestPrice)
        assertEquals("", state.highestPrice)
    }

    @Test
    fun `updateSelectedBrand updates brand`() = runTest {
        viewModel.updateSelectedBrand("apple")
        assertEquals("apple", viewModel.uiState.value.selectedBrand)
    }

    @Test
    fun `updateSelectedSort updates sort`() = runTest {
        viewModel.updateSelectedSort("popularity")
        assertEquals("popularity", viewModel.uiState.value.selectedSort)
    }

    @Test
    fun `updatePriceLowest updates lowestPrice`() = runTest {
        viewModel.updatePriceLowest("50")
        assertEquals("50", viewModel.uiState.value.lowestPrice)
    }

    @Test
    fun `updatePriceHighest updates highestPrice`() = runTest {
        viewModel.updatePriceHighest("300")
        assertEquals("300", viewModel.uiState.value.highestPrice)
    }

    @Test
    fun `toggleClickedGrid toggles from false to true`() = runTest {
        // Initially isClickedGrid = false (default)
        viewModel.toggleClickedGrid()

        val state = viewModel.uiState.value
        assertTrue(state.isClickedGrid)
    }

    @Test
    fun `toggleClickedGrid toggles from true to false`() = runTest {
        // First toggle it to true
        viewModel.toggleClickedGrid()
        assertTrue(viewModel.uiState.value.isClickedGrid)

        // Now toggle back
        viewModel.toggleClickedGrid()
        assertFalse(viewModel.uiState.value.isClickedGrid)
    }

    @Test
    fun `searchQuery updates search field in product filter`() = runTest {
        viewModel.searchQuery("laptop")

        val state = viewModel.uiState.value
        assertEquals("laptop", state.productFilter.search)
    }

    @Test
    fun `searchQuery sets search to null`() = runTest {
        viewModel.searchQuery(null)

        val state = viewModel.uiState.value
        assertNull(state.productFilter.search)
    }

    @Test
    fun `setQuery updates productFilter and logs analytics`() = runTest {
        every { productAnalyticsManager.trackFilterButtonClicked() } just Runs
        every { productAnalyticsManager.trackApplyFilter(any(), any(), any()) } just Runs

        viewModel.setQuery("Nike", "100", "500", "price_asc")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Nike", state.productFilter.brand)
            assertEquals(100, state.productFilter.lowest)
            assertEquals(500, state.productFilter.highest)
            assertEquals("price_asc", state.productFilter.sort)
        }
    }

    @Test
    fun `resetQuery clears productFilter`() = runTest {
        every { productAnalyticsManager.trackFilterResetButtonClicked() } just Runs

        viewModel.setQuery("Adidas", "50", "200", "price_desc")
        viewModel.resetQuery()

        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.productFilter.brand)
            assertNull(state.productFilter.lowest)
            assertNull(state.productFilter.highest)
            assertNull(state.productFilter.sort)
        }
    }

    @Test
    fun `fetchSearchSuggestions emits success and updates UI state`() = runTest {
        val query = "lenovo"
        val suggestions = listOf("lenovo1", "lenovo2")

        every { productAnalyticsManager.trackSearchButtonClicked() } just Runs
        every { productAnalyticsManager.trackSearchSuccess(query, suggestions.size) } just Runs
        every { productAnalyticsManager.trackViewSearchResults(query, suggestions) } just Runs

        val successFlow = flowOf(
            EcommerceResponse.Loading,
            EcommerceResponse.Success(suggestions)
        )

        coEvery { getSearchProductUseCase.invoke(query) } returns successFlow

        viewModel.searchQuery(query)
        viewModel.fetchSearchSuggestions()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        with(state.searchSuggestionState) {
            assertTrue(isSuccess)
            assertFalse(isLoading)
            assertFalse(isError)
            assertEquals(suggestions, this.suggestions)
        }
    }

    @Test
    fun `fetchSearchSuggestions emits failure and shows error`() = runTest {
        val query = "invalid"
        val error = "Network error"

        every { productAnalyticsManager.trackSearchButtonClicked() } just Runs
        every { productAnalyticsManager.trackSearchFailed(query, error) } just Runs

        val failureFlow = flowOf(
            EcommerceResponse.Loading,
            EcommerceResponse.Failure(400,error)
        )

        coEvery { getSearchProductUseCase.invoke(query) } returns failureFlow

        viewModel.searchQuery(query)
        viewModel.fetchSearchSuggestions()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        with(state.searchSuggestionState) {
            assertTrue(isError)
            assertFalse(isLoading)
            assertFalse(isSuccess)
        }
        assertEquals(error, state.userMessage)
    }

    @Test
    fun `getProductsFilter emits when productFilter changes`() = runTest {
        val expectedItems = listOf(Product(
            productId = "1", productName = "product", productPrice = 15000000,
            image = "image", brand = "brand",store ="store",sale = 5, productRating = 4.5F
        ), Product( productId = "2", productName = "product2", productPrice = 15000000,
            image = "image2", brand = "bran2d",store ="store2",sale = 5, productRating = 4.5F))
        val expectedPagingData = PagingData.from(expectedItems)

        coEvery {
            getProductFilterUseCase(
                search = "lenovo",
                brand = null,
                lowestPrice = null,
                highestPrice = null,
                sort = null
            )
        } returns flowOf(expectedPagingData)

        viewModel.searchQuery("lenovo")

        val differ = AsyncPagingDataDiffer(
            diffCallback = productDiffUtil,
            updateCallback = noOpListCallback,
            mainDispatcher = StandardTestDispatcher(testScheduler),
            workerDispatcher = StandardTestDispatcher(testScheduler)
        )

        val job = launch {
            viewModel.getProductsFilter.collectLatest {
                differ.submitData(it)
            }
        }

        advanceUntilIdle()

        assertEquals(expectedItems, differ.snapshot().items)
        job.cancel()
    }

    @Test
    fun `getProductsFilter applies price ascending sort correctly`() = runTest {
        val unsortedProducts = listOf(Product(
            productId = "1", productName = "product", productPrice = 15000,
            image = "image", brand = "brand",store ="store",sale = 5, productRating = 4.5F
        ), Product( productId = "2", productName = "product2", productPrice = 10000,
            image = "image2", brand = "brand2",store ="store2",sale = 5, productRating = 4.5F)
        , Product( productId = "3", productName = "product3", productPrice = 20000,
        image = "image3", brand = "brand3",store ="store3",sale = 5, productRating = 4.5F)
        )
        val expectedSorted = unsortedProducts.sortedBy { it.productPrice }
        val pagingData = PagingData.from(expectedSorted)

        coEvery {
            getProductFilterUseCase(
                search = null,
                brand = null,
                lowestPrice = null,
                highestPrice = null,
                sort = "Lowest"
            )
        } returns flowOf(pagingData)

        viewModel.setQuery(brand = null, lowest = null, highest = null, sort = "Lowest")

        val differ = AsyncPagingDataDiffer(
            diffCallback = productDiffUtil,
            updateCallback = noOpListCallback,
            mainDispatcher = StandardTestDispatcher(testScheduler),
            workerDispatcher = StandardTestDispatcher(testScheduler)
        )

        val job = launch {
            viewModel.getProductsFilter.collectLatest { paging ->
                differ.submitData(paging)
            }
        }

        advanceUntilIdle()

        val result = differ.snapshot().items
        assertEquals(3, result.size)
        assertEquals(10000, result[0].productPrice)
        assertEquals(15000, result[1].productPrice)
        assertEquals(20000, result[2].productPrice)

        job.cancel()
    }

    @Test
    fun `getProductsFilter applies price descending sort correctly`() = runTest {
        val unsortedProducts = listOf(Product(
            productId = "1", productName = "product", productPrice = 15000,
            image = "image", brand = "brand",store ="store",sale = 5, productRating = 4.5F
        ), Product( productId = "2", productName = "product2", productPrice = 10000,
            image = "image2", brand = "brand2",store ="store2",sale = 5, productRating = 4.5F)
            , Product( productId = "3", productName = "product3", productPrice = 20000,
                image = "image3", brand = "brand3",store ="store3",sale = 5, productRating = 4.5F)
        )
        val expectedSorted = unsortedProducts.sortedByDescending { it.productPrice }
        val pagingData = PagingData.from(expectedSorted)

        coEvery {
            getProductFilterUseCase(
                search = null,
                brand = null,
                lowestPrice = null,
                highestPrice = null,
                sort = "Highest"
            )
        } returns flowOf(pagingData)

        viewModel.setQuery(
            brand = null,
            lowest = null,
            highest = null,
            sort = "Highest"
        )

        val differ = AsyncPagingDataDiffer(
            diffCallback = productDiffUtil,
            updateCallback = noOpListCallback,
            mainDispatcher = StandardTestDispatcher(testScheduler),
            workerDispatcher = StandardTestDispatcher(testScheduler)
        )

        val job = launch {
            viewModel.getProductsFilter.collectLatest { paging ->
                differ.submitData(paging)
            }
        }

        advanceUntilIdle()

        val result = differ.snapshot().items
        assertEquals(3, result.size)
        assertEquals(20000, result[0].productPrice)
        assertEquals(15000, result[1].productPrice)
        assertEquals(10000, result[2].productPrice)

        job.cancel()
    }

    @Test
    fun `getProductsFilter filters products within price range 0 to 15000`() = runTest {
        val unsortedProducts = listOf(Product(
            productId = "1", productName = "product", productPrice = 15000,
            image = "image", brand = "brand",store ="store",sale = 5, productRating = 4.5F
        ), Product( productId = "2", productName = "product2", productPrice = 10000,
            image = "image2", brand = "brand2",store ="store2",sale = 5, productRating = 4.5F)
            , Product( productId = "3", productName = "product3", productPrice = 20000,
                image = "image3", brand = "brand3",store ="store3",sale = 5, productRating = 4.5F)
        )
        val expectedSorted = unsortedProducts.filter { it.productPrice in 0..15000 }
        val pagingData = PagingData.from(expectedSorted)

        coEvery {
            getProductFilterUseCase(
                search = null,
                brand = null,
                lowestPrice = 0,
                highestPrice = 15000,
                sort = null
            )
        } returns flowOf(pagingData)

        viewModel.setQuery(
            brand = null,
            lowest = "0",
            highest = "15000",
            sort = null
        )

        val differ = AsyncPagingDataDiffer(
            diffCallback = productDiffUtil,
            updateCallback = noOpListCallback,
            mainDispatcher = StandardTestDispatcher(testScheduler),
            workerDispatcher = StandardTestDispatcher(testScheduler)
        )

        val job = launch {
            viewModel.getProductsFilter.collectLatest { paging ->
                differ.submitData(paging)
            }
        }

        advanceUntilIdle()

        val result = differ.snapshot().items
        assertEquals(expectedSorted.size, result.size)
        job.cancel()
    }

}

private val productDiffUtil = object : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product) = oldItem.productId == newItem.productId
    override fun areContentsTheSame(oldItem: Product, newItem: Product) = oldItem == newItem
}

private val noOpListCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) = Unit
    override fun onRemoved(position: Int, count: Int) = Unit
    override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
    override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
}
