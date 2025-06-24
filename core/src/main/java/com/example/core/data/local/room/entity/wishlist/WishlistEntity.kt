package com.example.core.data.local.room.entity.wishlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.local.room.Constants

@Entity(tableName = Constants.Tables.WISHLIST)
data class WishlistEntity(
    @PrimaryKey()
    @ColumnInfo(name = Constants.Fields.PRODUCT_ID)
    val productId: String = "",

    @ColumnInfo(name = Constants.Fields.PRODUCT_NAME)
    val productName: String,

    @ColumnInfo(name = Constants.Fields.PRODUCT_IMAGE)
    val productImage: String,

    @ColumnInfo(name = Constants.Fields.PRODUCT_PRICE)
    val unitPrice: Int,

    @ColumnInfo(name = Constants.Fields.VARIANT_NAME)
    val variantName: String,

    @ColumnInfo(name = Constants.Fields.PRODUCT_STORE)
    val store: String,

    @ColumnInfo(name = Constants.Fields.PRODUCT_SALE)
    val sale: Int,

    @ColumnInfo(name = Constants.Fields.PRODUCT_RATING)
    val productRating: Float,

    @ColumnInfo(name = Constants.Fields.STOCK)
    val stock: Int,
)