package com.nathaniel.carryapp.domain.response

import com.nathaniel.carryapp.domain.enum.OrderStatus
import com.nathaniel.carryapp.domain.enum.PaymentMethod
import com.nathaniel.carryapp.domain.enum.PaymentStatus

data class OrderResponse(
    val productId: Long,
    val productName: String,
    val productImgUrl: String,
    val quantity: Int,
    val price: Double,
    val lineTotal: Double
)

data class OrderItemResponse(
    val productId: Long,
    val productName: String,
    val productImgUrl: String?,
    val quantity: Int,
    val price: Double,
    val lineTotal: Double
)

data class CustomerOrderResponse(

    val orderId: Long,
    val customerId: Long,

    val status: OrderStatus,
    val paymentMethod: PaymentMethod,

    val customerName: String,

    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val totalAmount: Double,

    val deliveryAddress: String,
    val notes: String?,

    val createdAt: String,

    val items: List<OrderItemResponse>,

    val riderId: Long?,
    val riderName: String?
)