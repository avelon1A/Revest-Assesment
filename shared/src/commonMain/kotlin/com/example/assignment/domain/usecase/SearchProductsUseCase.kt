package com.example.assignment.domain.usecase

import com.example.assignment.domain.model.Product
import com.example.assignment.domain.repository.ProductRepository

class SearchProductsUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(query: String): Result<List<Product>> = runCatching {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            repository.getProducts()
        } else {
            repository.searchProducts(trimmed)
        }
    }
}
