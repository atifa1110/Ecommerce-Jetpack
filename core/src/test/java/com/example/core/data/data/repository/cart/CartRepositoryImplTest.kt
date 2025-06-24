package com.example.core.data.data.repository.cart

import com.example.core.domain.model.CartModel
import com.example.core.data.local.datasource.CartDatabaseSource
import com.example.core.data.local.room.entity.cart.CartEntity
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.repository.cart.CartRepositoryImpl
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.collections.get
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImplTest {

    private val cartDatabaseSource = mockk<CartDatabaseSource>()
    lateinit var repository : CartRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = CartRepositoryImpl(cartDatabaseSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCartSize returns correct size for non-empty cart list`() = runTest {
        // Arrange
        val fakeEntity = CartEntity("1", "Product A", "img.png", "Variant", 10, 100, 1)
        val cartFlow = flowOf(listOf(fakeEntity, fakeEntity)) // 2 items
        coEvery { cartDatabaseSource.getAllCart() } returns cartFlow

        // Act
        val size = repository.getCartSize()

        // Assert
        assertEquals(2, size)
    }

    @Test
    fun `getCartSize returns 0 for empty cart list`() = runTest {
        // Arrange
        val cartFlow = flowOf(emptyList<CartEntity>())
        coEvery { cartDatabaseSource.getAllCart() } returns cartFlow

        // Act
        val size = repository.getCartSize()

        // Assert
        assertEquals(0, size)
    }

    @Test
    fun `getAllCart emits loading and success with cart data`() = runTest {
        val fakeEntity = CartEntity("1", "Product A", "img.png", "Variant", 10, 100, 1)
        val cartFlow = flowOf(listOf(fakeEntity))
        coEvery { cartDatabaseSource.getAllCart() } returns cartFlow

        val repository = CartRepositoryImpl(cartDatabaseSource)
        val emissions = repository.getAllCart().toList()

        assertEquals(EcommerceResponse.Loading, emissions[0])
        val success = emissions[1] as EcommerceResponse.Success
        assertEquals(1, success.value.size)
        assertEquals("1", success.value[0].productId)
    }

    @Test
    fun `getAllCart emits failure when exception occurs`() = runTest {
        coEvery { cartDatabaseSource.getAllCart() } throws RuntimeException("DB failure")

        val repository = CartRepositoryImpl(cartDatabaseSource)
        val emissions = repository.getAllCart().toList()

        assertEquals(EcommerceResponse.Loading, emissions[0])
        val failure = emissions[1] as EcommerceResponse.Failure
        assertEquals(-1, failure.code)
        assertEquals("DB failure", failure.error)
    }

    @Test
    fun `getAllCart emits success with empty list`() = runTest {
        val cartFlow = flowOf(emptyList<CartEntity>())
        coEvery { cartDatabaseSource.getAllCart() } returns cartFlow

        val emissions = repository.getAllCart().toList()

        assertEquals(EcommerceResponse.Loading, emissions[0])
        val success = emissions[1] as EcommerceResponse.Success
        assertTrue(success.value.isEmpty())
    }

    @Test
    fun `addToCart delegates to CartDatabaseSource`() = runTest {
        // Arrange
        val cartModel = CartModel("1", "Product A", "img", "Variant", 10, 100, 1)
        coEvery { cartDatabaseSource.insertOrUpdateCart(cartModel) } just Runs

        // Act
        repository.addToCart(cartModel)

        // Assert
        coVerify { cartDatabaseSource.insertOrUpdateCart(cartModel) }
    }


    @Test
    fun `removeFromCartById delegates to CartDatabaseSource`() = runTest {
        val cartModel = CartModel("1", "Product A", "img", "Variant", 10, 100, 1)
        // Arrange
        coEvery { cartDatabaseSource.insertOrUpdateCart(cartModel) } just Runs
        coEvery { cartDatabaseSource.deleteCartById("1") } just Runs

        // Act
        repository.addToCart(cartModel)
        repository.removeFromCartById("1")

        // Assert
        coVerify { cartDatabaseSource.deleteCartById("1") }
    }

    @Test
    fun `removeFromCart delegates to CartDatabaseSource`() = runTest {
        // Arrange
        val cartModel = CartModel("1", "Product A", "img", "Variant", 10, 100, 1)

        // Arrange
        coEvery { cartDatabaseSource.insertOrUpdateCart(cartModel) } just Runs
        coEvery { cartDatabaseSource.deleteCart(cartModel) } just Runs

        // Act
        repository.addToCart(cartModel)
        repository.removeFromCart(cartModel)

        // Assert
        coVerify { cartDatabaseSource.deleteCart(cartModel) }
    }

    @Test
    fun `removeAllCart delegates to CartDatabaseSource`() = runTest {
        // Arrange
        val cartModel = CartModel("1", "Product A", "img", "Variant", 10, 100, 1)
        coEvery { cartDatabaseSource.insertOrUpdateCart(cartModel) } just Runs
        coEvery { cartDatabaseSource.clearAllCart() } just Runs

        // Act
        repository.addToCart(cartModel)
        repository.removeAllCart()

        // Assert
        coVerify { cartDatabaseSource.clearAllCart() }
    }

}