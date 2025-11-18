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