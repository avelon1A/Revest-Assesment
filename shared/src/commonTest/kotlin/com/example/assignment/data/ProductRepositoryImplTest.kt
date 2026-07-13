package com.example.assignment.data

import com.example.assignment.data.remote.ProductApi
import com.example.assignment.data.remote.createHttpClient
import com.example.assignment.data.repository.ProductRepositoryImpl
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ProductRepositoryImplTest {

    private fun jsonEngine(handler: (path: String, query: String) -> String) = MockEngine { request ->
        val body = handler(request.url.encodedPath, request.url.encodedQuery)
        respond(
            content = body,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
        )
    }

    private fun repository(engine: MockEngine) =
        ProductRepositoryImpl(ProductApi(createHttpClient(engine)))

    @Test
    fun searchProducts_parsesEnvelope_andMapsToDomain() = runTest {
        val engine = jsonEngine { path, _ ->
            check(path.endsWith("/products/search")) { "unexpected path: $path" }
            """
            {
              "products": [
                {"id": 1, "title": "iPhone", "price": 999.0, "rating": 4.5, "thumbnail": "t1"},
                {"id": 2, "title": "iPad", "price": 799.5, "rating": 4.7, "thumbnail": "t2"}
              ],
              "total": 2, "skip": 0, "limit": 30
            }
            """.trimIndent()
        }

        val products = repository(engine).searchProducts("i")

        assertEquals(2, products.size)
        assertEquals("iPhone", products[0].title)
        assertEquals(999.0, products[0].price)
        assertEquals(4.7, products[1].rating)
    }

    @Test
    fun getProduct_parsesSingleObject_withMissingFieldsDefaulted() = runTest {
        val engine = jsonEngine { path, _ ->
            check(path.endsWith("/products/1")) { "unexpected path: $path" }
            """{"id": 1, "title": "iPhone", "description": "A phone", "price": 999.0, "category": "smartphones", "thumbnail": "t1"}"""
        }

        val product = repository(engine).getProduct(1)

        assertEquals(1, product.id)
        assertEquals("A phone", product.description)
        assertEquals("smartphones", product.category)
        assertEquals(null, product.brand)
        assertEquals(emptyList(), product.images)
    }

    @Test
    fun getCategories_parsesStringArray() = runTest {
        val engine = jsonEngine { path, _ ->
            check(path.endsWith("/products/category-list")) { "unexpected path: $path" }
            """["beauty", "fragrances", "furniture"]"""
        }

        val categories = repository(engine).getCategories()

        assertEquals(listOf("beauty", "fragrances", "furniture"), categories)
    }
}
