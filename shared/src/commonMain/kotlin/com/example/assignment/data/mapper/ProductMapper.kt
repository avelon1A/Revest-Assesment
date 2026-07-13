package com.example.assignment.data.mapper

import com.example.assignment.data.remote.dto.ProductDto
import com.example.assignment.domain.model.Product

fun ProductDto.toDomain(): Product = Product(
    id = id,
    title = title,
    description = description,
    price = price,
    discountPercentage = discountPercentage,
    rating = rating,
    brand = brand,
    category = category,
    thumbnail = thumbnail,
    images = images,
)

fun List<ProductDto>.toDomain(): List<Product> = map { it.toDomain() }
