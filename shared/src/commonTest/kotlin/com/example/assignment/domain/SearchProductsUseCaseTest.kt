package com.example.assignment.domain

import com.example.assignment.domain.model.Product
import com.example.assignment.domain.repository.ProductRepository
import com.example.assignment.domain.usecase.SearchProductsUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SearchProductsUseCaseTest {

    private fun product(id: Int, title: String) = Product(
        id = id,
        title = title,
        description = "",
        price = 1.0,
        discountPercentage = 0.0,
        rating = 4.0,
        brand = null,
        category = "misc",
        thumbnail = "",
        images = emptyList(),
    )

    private class FakeProductRepository(
        private val all: List<Product>,
        private val failWith: Throwable? = null,
    ) : ProductRepository {
        var lastSearchQuery: String? = null
        var getProductsCalled = false

        override suspend fun getProducts(): List<Product> {
            failWith?.let { throw it }
            getProductsCalled = true
            return all
        }

        override suspend fun searchProducts(query: String): List<Product> {
            failWith?.let { throw it }
            lastSearchQuery = query
            return all.filter { it.title.contains(query, ignoreCase = true) }
        }

        override suspend fun getProduct(id: Int) = all.first { it.id == id }
        override suspend fun getCategories() = emptyList<String>()
        override suspend fun getProductsByCategory(category: String) = all
    }

    private val catalog = listOf(product(1, "iPhone 15"), product(2, "Galaxy S24"))

    @Test
    fun blankQuery_returnsFullCatalog_withoutHittingSearch() = runTest {
        val repo = FakeProductRepository(catalog)
        val useCase = SearchProductsUseCase(repo)

        val result = useCase("   ")

        assertEquals(catalog, result.getOrNull())
        assertTrue(repo.getProductsCalled, "blank query should fall back to getProducts()")
        assertNull(repo.lastSearchQuery, "blank query should not call searchProducts()")
    }

    @Test
    fun nonBlankQuery_delegatesToSearch_withTrimmedQuery() = runTest {
        val repo = FakeProductRepository(catalog)
        val useCase = SearchProductsUseCase(repo)

        val result = useCase("  iPhone  ")

        assertEquals("iPhone", repo.lastSearchQuery)
        assertEquals(listOf(product(1, "iPhone 15")), result.getOrNull())
    }

    @Test
    fun repositoryError_isCapturedAsResultFailure() = runTest {
        val repo = FakeProductRepository(catalog, failWith = IllegalStateException("network down"))
        val useCase = SearchProductsUseCase(repo)

        val result = useCase("phone")

        assertTrue(result.isFailure)
        assertEquals("network down", result.exceptionOrNull()?.message)
    }
}
