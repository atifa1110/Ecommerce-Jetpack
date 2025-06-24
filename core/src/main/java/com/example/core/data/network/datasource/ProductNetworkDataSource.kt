package com.example.core.data.network.datasource

import com.example.core.data.network.api.ProductApiService
import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.ProductResponse
import com.example.core.data.network.response.ReviewResponse
import com.example.core.data.network.response.SearchResponse
import com.example.core.data.network.utils.BaseRemoteDataSource
import com.google.gson.Gson
import javax.inject.Inject

class ProductNetworkDataSource @Inject constructor(
    private val productApiService: ProductApiService,
    gson : Gson
): BaseRemoteDataSource(gson){

    suspend fun getProductFilter(
        search: String?,
        brand: String?,
        lowestPrice: Int?,
        highestPrice: Int?,
        sort: String?,
        limit: Int,
        page: Int
    ): EcommerceResponse<ProductResponse> {
        return safeApiCall {
            productApiService.getProductFilter(search, brand, lowestPrice, highestPrice, sort, limit, page)
        }
    }

    suspend fun searchProductList(query: String): EcommerceResponse<SearchResponse> {
        return safeApiCall {
            productApiService.searchProductList(query)
        }
    }

    suspend fun getProductDetail(id: String): EcommerceResponse<DetailResponse> {
        return safeApiCall {
            productApiService.getProductDetail(id)
        }
    }

    suspend fun getProductReview(id: String): EcommerceResponse<ReviewResponse> {
        return safeApiCall {
            productApiService.getProductReview(id)
        }
    }

}