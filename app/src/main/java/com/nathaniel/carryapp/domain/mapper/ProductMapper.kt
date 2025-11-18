package com.nathaniel.carryapp.domain.mapper

import com.nathaniel.carryapp.domain.model.Product
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
            description = dto.produceDescription,
            stocks = dto.stocks,
            category = dto.categoryName
        )
    }

    fun toDomainList(list: List<ProductResponse>) =
        list.map { toDomain(it) }
}