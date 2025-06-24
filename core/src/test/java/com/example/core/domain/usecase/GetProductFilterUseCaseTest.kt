package com.example.core.domain.usecase

import androidx.paging.PagingData
import com.example.core.domain.model.ProductModel
import com.example.core.domain.repository.product.ProductRepository
import com.example.core.ui.mapper.asProduct
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.core.ui.model.Product
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetProductFilterUseCaseTest {

    private lateinit var repository: ProductRepository
    private lateinit var useCase: GetProductFilterUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetProductFilterUseCase(repository)
    }

    @Test
    fun `invoke returns mapped PagingData of Product`() = runTest {
        // Given
        val productModel = ProductModel(
            productId = "1",
            productName = "name",
            productPrice = 1000,
            image = "image",
            brand = "brand",
            store = "store",
            sale = 0,
            productRating = 4F
        )
        val expectedProduct = productModel.asProduct()

        val pagingData = PagingData.from(listOf(productModel))
        coEvery {
            repository.getProductFilter("", "brand", null, null, null)
        } returns flowOf(pagingData)

        // When
        val resultFlow = useCase("", "brand", null, null, null)
        val differ = AsyncPagingDataDiffer(
            diffCallback = productDiffUtil,
            updateCallback = noOpListCallback,
            mainDispatcher = StandardTestDispatcher(testScheduler),
            workerDispatcher = StandardTestDispatcher(testScheduler)
        )
        differ.submitData(resultFlow.first())
        val actual = differ.snapshot().items

        // Then
        assertEquals(listOf(expectedProduct), actual)
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
