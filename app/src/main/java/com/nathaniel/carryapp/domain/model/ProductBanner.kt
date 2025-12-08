package com.nathaniel.carryapp.domain.model

data class ProductBannerResponse(
    val bannerId: Long,
    val bannerUrl: String,
    val bannerUrlLink: String,
    val createdAt: String
)

data class ProductBanner(
    val id: Long,
    val bannerUrl: String,
    val bannerUrlLink: String,
)

object ProductBannerMapper {
    fun toDomain(dto: ProductBannerResponse): ProductBanner {
        return ProductBanner(
            id = dto.bannerId,
            bannerUrl = dto.bannerUrl,
            bannerUrlLink = dto.bannerUrlLink
        )
    }

    fun toDomainList(list: List<ProductBannerResponse>): List<ProductBanner> {
        return list
            .map { toDomain(it) }
    }
}

data class SnowballPromo(
    val id: Long,
    val title: String,
    val reward: String,
    val requiredQty: Int,
    val hasExpiry: Boolean,
    val expiry: String?,
    val terms: String,
    val products: List<ProductItem>,
    val promoPrices: Map<Long, Double>
)

data class ProductItem(
    val productId: Long,
    val name: String,
    val categoryName: String?,
    val imageUrl: String?
)