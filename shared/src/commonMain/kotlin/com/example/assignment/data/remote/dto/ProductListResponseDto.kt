package com.example.assignment.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponseDto(
    val products: List<ProductDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)
