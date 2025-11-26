package com.nathaniel.carryapp.domain.response

data class ProductResponse(
    val priceId: Long,
    val productId: Long,
    val basePrice: Double,
    val effectiveDate: String,
    val productName: String,
    val productCode: String,
    val productSize: String,
    val productImgUrl: String,
    val productDescription: String,
    val productStatus: String?,
    val expiryDate: String?,
    val productInDate: String?,
    val stocks: Int,
    val categoryId: Long?,
    val categoryName: String
)