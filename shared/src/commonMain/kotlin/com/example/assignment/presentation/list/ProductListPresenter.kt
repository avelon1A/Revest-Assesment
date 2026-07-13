package com.example.assignment.presentation.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.assignment.domain.model.Product
import com.example.assignment.domain.usecase.GetCategoriesUseCase
import com.example.assignment.domain.usecase.GetProductsByCategoryUseCase
import com.example.assignment.domain.usecase.GetProductsUseCase
import com.example.assignment.domain.usecase.SearchProductsUseCase
import com.example.assignment.presentation.navigation.ProductDetailScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.delay

class ProductListPresenter(
    private val navigator: Navigator,
    private val getProducts: GetProductsUseCase,
    private val searchProducts: SearchProductsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getProductsByCategory: GetProductsByCategoryUseCase,
) : Presenter<ProductListState> {

    @Composable
    override fun present(): ProductListState {
        var query by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf<String?>(null) }
        var reloadToken by remember { mutableStateOf(0) }

        var products by remember { mutableStateOf<List<Product>>(emptyList()) }
        var categories by remember { mutableStateOf<List<String>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(Unit) {
            getCategories().onSuccess { categories = it }
        }

        LaunchedEffect(query, selectedCategory, reloadToken) {
            isLoading = true
            errorMessage = null

            if (query.isNotBlank()) delay(SEARCH_DEBOUNCE_MS)

            val result = when {
                query.isNotBlank() -> searchProducts(query)
                selectedCategory != null -> getProductsByCategory(selectedCategory!!)
                else -> getProducts()
            }

            result
                .onSuccess {
                    products = it
                    isLoading = false
                }
                .onFailure {
                    errorMessage = it.message ?: "Something went wrong. Please try again."
                    isLoading = false
                }
        }

        return ProductListState(
            products = products,
            query = query,
            categories = categories,
            selectedCategory = selectedCategory,
            isLoading = isLoading,
            errorMessage = errorMessage,
        ) { event ->
            when (event) {
                is ProductListEvent.QueryChanged -> {
                    query = event.query
                    if (event.query.isNotBlank()) selectedCategory = null
                }
                is ProductListEvent.CategorySelected -> {
                    selectedCategory = event.category
                    query = ""
                }
                is ProductListEvent.ProductClicked ->
                    navigator.goTo(ProductDetailScreen(event.productId))
                ProductListEvent.Retry -> reloadToken++
            }
        }
    }

    private companion object {
        const val SEARCH_DEBOUNCE_MS = 350L
    }
}
