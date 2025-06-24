package com.example.core.domain.usecase

import com.example.core.domain.model.ProductDetailModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(id: String): Flow<EcommerceResponse<ProductDetailModel>> {
        return productRepository.detailProduct(id)
    }
}