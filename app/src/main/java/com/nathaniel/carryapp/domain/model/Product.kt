package com.nathaniel.carryapp.domain.model

data class Product(
    val id: Long,
    val name: String,
    val code: String,
    val size: String,
    val price: Double,
    val imageUrl: String,
    val productDescription: String,
    val stocks: Int,
    val categoryName: String
)

data class ShopProduct(
    val id: Long,
    val name: String,
    val weight: String,
    val productDescription: String,
    val sold: Int,
    val price: Double,
    val imageUrl: String,
    val categoryName: String,

    val enabled: Boolean = true
)

data class ProductRack(
    val title: String,
    val products: List<ShopProduct>
)

data class CartDisplayItem(
    val productId: Long,
    val name: String,
    val imageUrl: String,
    val weight: String,
    val price: Double,
    val qty: Int,
    val subtotal: Double
)