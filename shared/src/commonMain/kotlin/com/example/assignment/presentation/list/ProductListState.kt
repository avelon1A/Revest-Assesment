package com.example.assignment.presentation.list

import androidx.compose.runtime.Immutable
import com.example.assignment.domain.model.Product
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class ProductListState(
    val products: List<Product>,
    val query: String,
    val categories: List<String>,
    val selectedCategory: String?,
    val isLoading: Boolean,
    val errorMessage: String?,
    val eventSink: (ProductListEvent) -> Unit,
) : CircuitUiState

sealed interface ProductListEvent : CircuitUiEvent {
    data class QueryChanged(val query: String) : ProductListEvent
    data class CategorySelected(val category: String?) : ProductListEvent
    data class ProductClicked(val productId: Int) : ProductListEvent
    data object Retry : ProductListEvent
}
