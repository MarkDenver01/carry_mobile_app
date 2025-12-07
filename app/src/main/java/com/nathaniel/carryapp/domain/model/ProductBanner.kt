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