package com.nathaniel.carryapp.domain.model

data class Product(
    val id: Long,
    val name: String,
    val code: String,
    val size: String,
    val price: Double,
    val imageUrl: String,
    val description: String,
    val stocks: Int,
    val category: String
)

data class ShopProduct(
    val id: String,
    val name: String,
    val weight: String,
    val description: String,
    val sold: String,
    val price: String,
    val imageUrl: String,
    val category: String,
)

data class ProductRack(
    val title: String,
    val products: List<ShopProduct>
)

data class CartSummary(
    val productId: Long,
    val name: String,
    val imageUrl: String,
    val weight: String,
    val price: Double,
    val qty: Int,
    val subtotal: Double
)