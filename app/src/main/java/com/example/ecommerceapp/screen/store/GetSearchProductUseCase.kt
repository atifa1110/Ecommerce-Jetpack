package com.example.ecommerceapp.screen.store

import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(query: String): Flow<EcommerceResponse<List<String>>> {
        return productRepository.searchProductList(query)
    }
}