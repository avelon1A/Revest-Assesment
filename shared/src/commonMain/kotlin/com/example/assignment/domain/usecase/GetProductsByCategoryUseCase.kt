package com.example.assignment.domain.usecase

import com.example.assignment.domain.model.Product
import com.example.assignment.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(category: String): Result<List<Product>> = runCatching {
        repository.getProductsByCategory(category)
    }
}
