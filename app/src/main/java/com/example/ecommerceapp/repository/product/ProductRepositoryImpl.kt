package com.example.ecommerceapp.repository.product

import android.R
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ecommerceapp.data.domain.ProductDetailModel
import com.example.ecommerceapp.data.domain.ProductModel
import com.example.ecommerceapp.data.domain.ReviewModel
import com.example.ecommerceapp.data.domain.mapper.asProductDetailModel
import com.example.ecommerceapp.data.domain.mapper.asReviewModel
import com.example.ecommerceapp.data.local.datasource.WishlistDatabaseSource
import com.example.ecommerceapp.data.network.datasource.ProductNetworkDataSource
import com.example.ecommerceapp.data.network.paging.ProductPagingSource
import com.example.ecommerceapp.data.network.response.EcommerceResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productNetworkDataSource: ProductNetworkDataSource,
    private val wishlistDatabaseSource: WishlistDatabaseSource
) : ProductRepository{

    override fun searchProductList(query: String): Flow<EcommerceResponse<List<String>>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(2000L)
            when (val result = productNetworkDataSource.searchProductList(query)) {
                is EcommerceResponse.Success -> emit(EcommerceResponse.Success(result.value.data))
                is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(result.code, result.error))
                else->{}
            }
        }
    }

    override fun getProductFilter(
        search: String?,
        brand: String?,
        lowestPrice: Int?,
        highestPrice: Int?,
        sort: String?,
    ): Flow<PagingData<ProductModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                ProductPagingSource(
                    dataSource = productNetworkDataSource,
                    search = search,
                    brand = brand,
                    lowest = lowestPrice,
                    highest = highestPrice,
                    sort= sort,
                )
            }
        ).flow
    }

    override fun detailProduct(id:String): Flow<EcommerceResponse<ProductDetailModel>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(2000L)
            when (val result = productNetworkDataSource.getProductDetail(id)) {
                is EcommerceResponse.Success -> {
                    val isWishListed = wishlistDatabaseSource.isWishListed(id)
                    emit(EcommerceResponse.Success(result.value.data.asProductDetailModel(isWishListed)))
                }
                is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(result.code, result.error))
                else->{}
            }
        }
    }

    override fun getRatingProduct(id: String): Flow<EcommerceResponse<List<ReviewModel>>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(1500L)
            when (val result = productNetworkDataSource.getProductReview(id)) {
                is EcommerceResponse.Success -> {
                    val data = result.value.data?.map { it.asReviewModel()}
                    emit(EcommerceResponse.Success(data?:emptyList()))
                }
                is EcommerceResponse.Failure -> emit(EcommerceResponse.Failure(result.code, result.error))
                else->{}
            }
        }
    }

}