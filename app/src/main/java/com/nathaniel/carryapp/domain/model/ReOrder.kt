package com.nathaniel.carryapp.domain.model

import com.nathaniel.carryapp.data.local.room.entity.ReorderEntity

fun ReorderEntity.toShopProduct(): ShopProduct {
    return ShopProduct(
        id = productId,
        name = name,
        productDescription = "",
        weight = weight,
        sold = qty,
        price = price,
        imageUrl = imageUrl,
        categoryName = categoryName,
        expiryDate = expiryDate,
        inDate = inDate
    )
}