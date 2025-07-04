package com.example.core.data.network.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.mapper.asProductModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.data.network.response.PagingError
import com.example.core.domain.model.ProductModel
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class ProductPagingNetworkSource(
    private val dataSource: ProductNetworkDataSource,
    private val search : String?,
    private val brand : String?,
    private val lowest : Int?,
    private val highest : Int?,
    private val sort : String?
) : PagingSource<Int, ProductModel>() {

    override fun getRefreshKey(state: PagingState<Int, ProductModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductModel> {
        return try {
            delay(2000)
            val page = params.key ?: 1
            val response = dataSource.getProductFilter(search, brand, lowest, highest, sort, 10, page)

            when (response) {
                is EcommerceResponse.Failure -> {
                    if (response.code == 404) {
                        LoadResult.Error(PagingError.NotFoundError)
                    } else {
                        LoadResult.Error(PagingError.ApiError(response.code, response.error))
                    }
                }
                is EcommerceResponse.Success -> {
                    val data = response.value.data.items.map { it.asProductModel() }
                    val endOfPaginationReached = data.isEmpty()

                    LoadResult.Page(
                        data = data,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1
                    )
                }
                else -> LoadResult.Error(Exception("Unexpected response"))
            }
        } catch (e: IOException) {
            LoadResult.Error(PagingError.ConnectionError)
        } catch (e: HttpException) {
            LoadResult.Error(PagingError.ApiError(e.code(), e.message()))
        }
    }

}