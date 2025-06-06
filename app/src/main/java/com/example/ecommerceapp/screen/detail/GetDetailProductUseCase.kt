package com.example.ecommerceapp.screen.detail

import com.example.ecommerceapp.data.domain.ProductDetailModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(id: String): Flow<EcommerceResponse<ProductDetailModel>> {
        return productRepository.detailProduct(id)
    }
}