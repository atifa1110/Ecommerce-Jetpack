package com.example.core.domain.usecase

import com.example.core.domain.model.ReviewModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke(id: String): Flow<EcommerceResponse<List<ReviewModel>>> {
        return productRepository.getRatingProduct(id)
    }
}