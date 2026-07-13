package com.example.assignment.domain.repository

import com.example.assignment.domain.model.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProduct(id: Int): Product
    suspend fun getCategories(): List<String>
    suspend fun getProductsByCategory(category: String): List<Product>
}
