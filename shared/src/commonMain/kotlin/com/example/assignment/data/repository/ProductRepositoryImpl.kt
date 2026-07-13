package com.example.assignment.data.repository

import com.example.assignment.data.mapper.toDomain
import com.example.assignment.data.remote.ProductApi
import com.example.assignment.domain.model.Product
import com.example.assignment.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val api: ProductApi,
) : ProductRepository {

    override suspend fun getProducts(): List<Product> =
        api.getProducts().products.toDomain()

    override suspend fun searchProducts(query: String): List<Product> =
        api.searchProducts(query).products.toDomain()

    override suspend fun getProduct(id: Int): Product =
        api.getProduct(id).toDomain()

    override suspend fun getCategories(): List<String> =
        api.getCategoryList()

    override suspend fun getProductsByCategory(category: String): List<Product> =
        api.getProductsByCategory(category).products.toDomain()
}
