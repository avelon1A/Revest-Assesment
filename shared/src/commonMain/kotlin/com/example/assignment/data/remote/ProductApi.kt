package com.example.assignment.data.remote

import com.example.assignment.data.remote.dto.ProductDto
import com.example.assignment.data.remote.dto.ProductListResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.encodeURLPath

class ProductApi(
    private val client: HttpClient,
    private val baseUrl: String = DEFAULT_BASE_URL,
) {
    suspend fun getProducts(limit: Int = DEFAULT_LIMIT): ProductListResponseDto =
        client.get("$baseUrl/products") {
            parameter("limit", limit)
        }.body()

    suspend fun searchProducts(query: String): ProductListResponseDto =
        client.get("$baseUrl/products/search") {
            parameter("q", query)
        }.body()

    suspend fun getProduct(id: Int): ProductDto =
        client.get("$baseUrl/products/$id").body()

    suspend fun getCategoryList(): List<String> =
        client.get("$baseUrl/products/category-list").body()

    suspend fun getProductsByCategory(slug: String): ProductListResponseDto =
        client.get("$baseUrl/products/category/${slug.encodeURLPath()}").body()

    companion object {
        const val DEFAULT_BASE_URL = "https://dummyjson.com"
        const val DEFAULT_LIMIT = 30
    }
}
