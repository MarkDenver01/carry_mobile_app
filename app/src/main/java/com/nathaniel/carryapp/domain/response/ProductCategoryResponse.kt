package com.nathaniel.carryapp.domain.response

data class ProductCategoryResponse(
    val categoryId: Long,
    val categoryName: String,
    val categoryDescription: String
)