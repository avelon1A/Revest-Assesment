package com.example.assignment.domain.usecase

import com.example.assignment.domain.model.Product
import com.example.assignment.domain.repository.ProductRepository

class GetProductsUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(): Result<List<Product>> = runCatching {
        repository.getProducts()
    }
}
