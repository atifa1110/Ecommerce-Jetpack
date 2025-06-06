package com.example.ecommerceapp.screen.review

import com.example.ecommerceapp.data.domain.ReviewModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import com.example.ecommerceapp.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReviewProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    operator fun invoke(id: String): Flow<EcommerceResponse<List<ReviewModel>>>{
        return productRepository.getRatingProduct(id)
    }
}