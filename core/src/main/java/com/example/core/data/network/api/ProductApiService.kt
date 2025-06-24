package com.example.core.data.network.api

import com.example.core.data.network.response.DetailResponse
import com.example.core.data.network.response.ProductResponse
import com.example.core.data.network.response.ReviewResponse
import com.example.core.data.network.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApiService {

    @POST("products")
    suspend fun getProductFilter(
        @Query("search") search: String?,
        @Query("brand") brand: String?,
        @Query("lowest") lowestPrice: Int?,
        @Query("highest") highestPrice: Int?,
        @Query("sort") sort: String?,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<ProductResponse>

    @POST("search")
    suspend fun searchProductList(
        @Query("query") query: String
    ): Response<SearchResponse>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: String
    ): Response<DetailResponse>

    @GET("review/{id}")
    suspend fun getProductReview(
        @Path("id") id: String
    ): Response<ReviewResponse>
}
