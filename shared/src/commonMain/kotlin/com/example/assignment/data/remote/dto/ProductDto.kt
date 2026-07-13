package com.example.assignment.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Int,
    val title: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val discountPercentage: Double = 0.0,
    val rating: Double = 0.0,
    val brand: String? = null,
    val category: String = "",
    val thumbnail: String = "",
    val images: List<String> = emptyList(),
)
