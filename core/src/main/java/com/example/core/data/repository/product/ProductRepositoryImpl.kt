package com.example.core.data.repository.product

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.domain.model.ProductDetailModel
import com.example.core.domain.model.ProductModel
import com.example.core.domain.model.ReviewModel
import com.example.core.data.mapper.asProductDetailModel
import com.example.core.data.mapper.asReviewModel
import com.example.core.data.local.datasource.WishlistDatabaseSource
import com.example.core.data.network.datasource.ProductNetworkDataSource
import com.example.core.data.network.datasource.ProductPagingNetworkSource
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.product.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productNetworkDataSource: ProductNetworkDataSource,
    private val wishlistDatabaseSource: WishlistDatabaseSource
) : ProductRepository {

    override fun searchProductList(query: String): Flow<EcommerceResponse<List<String>>> {
        return flow {
            emit(EcommerceResponse.Loading)
            delay(1000L)
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
            config = PagingConfig(
                pageSize = 10, initialLoadSize = 10, prefetchDistance = 1
            ), pagingSourceFactory = {
                ProductPagingNetworkSource(
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
            delay(1000L)
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