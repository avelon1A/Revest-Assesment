package com.example.assignment.domain.usecase

import com.example.assignment.domain.model.Product
import com.example.assignment.domain.repository.ProductRepository

class GetProductDetailUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(id: Int): Result<Product> = runCatching {
        repository.getProduct(id)
    }
}
