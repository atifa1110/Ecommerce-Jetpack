package com.example.core.domain.repository.product

import androidx.paging.PagingData
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.model.ProductDetailModel
import com.example.core.domain.model.ProductModel
import com.example.core.domain.model.ReviewModel
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun searchProductList(query: String) : Flow<EcommerceResponse<List<String>>>
    fun getProductFilter(search: String?, brand: String?,
    lowestPrice: Int?, highestPrice: Int?, sort: String?) : Flow<PagingData<ProductModel>>
    fun detailProduct(id : String) : Flow<EcommerceResponse<ProductDetailModel>>
    fun getRatingProduct(id : String) : Flow<EcommerceResponse<List<ReviewModel>>>
}