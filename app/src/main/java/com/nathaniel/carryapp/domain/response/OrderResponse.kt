package com.nathaniel.carryapp.domain.response

data class OrderResponse(
    val productId: Long,
    val productName: String,
    val productImgUrl: String,
    val quantity: Int,
    val price: Double,
    val lineTotal: Double
)