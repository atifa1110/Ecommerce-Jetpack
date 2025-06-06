package com.example.ecommerceapp.repository.product

import androidx.paging.PagingData
import com.example.ecommerceapp.data.domain.ProductDetailModel
import com.example.ecommerceapp.data.domain.ProductModel
import com.example.ecommerceapp.data.domain.ReviewModel
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun searchProductList(query: String) : Flow<EcommerceResponse<List<String>>>
    fun getProductFilter(search: String?, brand: String?,
    lowestPrice: Int?, highestPrice: Int?, sort: String?) : Flow<PagingData<ProductModel>>
    fun detailProduct(id : String) : Flow<EcommerceResponse<ProductDetailModel>>
    fun getRatingProduct(id : String) : Flow<EcommerceResponse<List<ReviewModel>>>
}