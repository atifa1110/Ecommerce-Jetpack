package com.example.core.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.ui.model.Product
import com.example.core.ui.mapper.asProduct
import com.example.core.domain.repository.product.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductFilterUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(search: String?,
                        brand: String?,
                        lowestPrice: Int?,
                        highestPrice: Int?,
                        sort: String?): Flow<PagingData<Product>> {
        return productRepository.getProductFilter(search,brand,lowestPrice,highestPrice,sort).map { pagingData->
            pagingData.map { productModel->
                productModel.asProduct()
            }
        }
    }
}
