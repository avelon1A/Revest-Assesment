package com.example.assignment.domain.usecase

import com.example.assignment.domain.repository.ProductRepository

class GetCategoriesUseCase(
    private val repository: ProductRepository,
) {
    suspend operator fun invoke(): Result<List<String>> = runCatching {
        repository.getCategories()
    }
}
