package com.nathaniel.carryapp.presentation.ui.compose.orders.model

import com.nathaniel.carryapp.R
import com.nathaniel.carryapp.domain.model.Product

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

// DOMAIN -> UI MAPPER
fun Product.toShopProduct(): ShopProduct {
    return ShopProduct(
        id = id.toString(),
        name = name,
        description = description,
        weight = size,
        sold = "0",
        price = "₱$price",
        imageUrl = imageUrl,
        category = category
    )
}

data class ProductRack(
    val title: String,
    val products: List<ShopProduct>
)