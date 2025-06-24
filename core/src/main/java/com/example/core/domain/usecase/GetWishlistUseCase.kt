package com.example.core.domain.usecase

import com.example.core.domain.model.WishlistModel
import com.example.core.data.network.response.EcommerceResponse
import com.example.core.domain.repository.wishlist.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWishlistUseCase @Inject constructor(
    private val wishlistRepository: WishlistRepository
) {
    operator fun invoke () : Flow<EcommerceResponse<List<WishlistModel>>> {
        return wishlistRepository.getWishlist()
    }
}