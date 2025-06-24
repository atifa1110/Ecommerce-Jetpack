package com.example.core.data.local.room.entity.cart

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.data.local.room.Constants

@Entity(tableName = Constants.Tables.CART)
data class CartEntity(
    @PrimaryKey
    @ColumnInfo(name = Constants.Fields.PRODUCT_ID)
    val productId: String,
    @ColumnInfo(name = Constants.Fields.PRODUCT_NAME)
    val productName: String,
    @ColumnInfo(name = Constants.Fields.PRODUCT_IMAGE)
    val image: String,
    @ColumnInfo(name = Constants.Fields.VARIANT_NAME)
    val variantName: String,
    @ColumnInfo(name = Constants.Fields.PRODUCT_PRICE)
    val unitPrice: Int,
    @ColumnInfo(name = Constants.Fields.QUANTITY)
    val quantity: Int,
    @ColumnInfo(name = Constants.Fields.STOCK)
    val stock: Int
)
