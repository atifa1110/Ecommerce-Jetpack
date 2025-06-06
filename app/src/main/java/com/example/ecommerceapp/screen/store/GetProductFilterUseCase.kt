package com.example.ecommerceapp.screen.store

import androidx.paging.PagingData
import androidx.paging.map
import com.example.ecommerceapp.data.domain.ProductModel
import com.example.ecommerceapp.data.ui.Product
import com.example.ecommerceapp.data.ui.mapper.asProduct
import com.example.ecommerceapp.repository.product.ProductRepository
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
