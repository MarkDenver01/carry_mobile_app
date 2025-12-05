package com.nathaniel.carryapp.domain.mapper

import com.nathaniel.carryapp.domain.model.Product
import com.nathaniel.carryapp.domain.model.ShopProduct
import com.nathaniel.carryapp.domain.response.ProductResponse

object ProductMapper {
    fun toDomain(dto: ProductResponse): Product {
        return Product(
            id = dto.productId,
            name = dto.productName,
            code = dto.productCode,
            size = dto.productSize,
            price = dto.basePrice,
            imageUrl = dto.productImgUrl,
            productDescription = dto.productDescription,
            stocks = dto.stocks,
            categoryName = dto.categoryName
        )
    }

    // Filter before mapping (more efficient)
    fun toDomainList(list: List<ProductResponse>): List<Product> {
        return list
            .filter { it.stocks > 0 }            // ⬅ Only map products with stocks > 0
            .map { toDomain(it) }
    }

    // DOMAIN -> UI MAPPER
    fun Product.toShopProduct(): ShopProduct {
        return ShopProduct(
            id = id,
            name = name,
            productDescription = productDescription,
            weight = size,
            sold = stocks,
            price = price,
            imageUrl = imageUrl,
            categoryName = categoryName
        )
    }
}