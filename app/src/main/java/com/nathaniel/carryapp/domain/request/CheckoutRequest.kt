package com.nathaniel.carryapp.domain.request

data class CheckoutRequest(
    val customerId: Long,
    val paymentMethod: String,   // "COD" or "WALLET"
    val deliveryFee: Double,
    val discount: Double,
    val deliveryAddress: String,
    val notes: String?,
    val items: List<CheckoutItemRequest>
)

data class CheckoutItemRequest(
    val productId: Long,
    val quantity: Int
)