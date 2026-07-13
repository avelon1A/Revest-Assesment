package com.example.assignment.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.assignment.domain.model.Product
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class ProductDetailState(
    val product: Product?,
    val isLoading: Boolean,
    val errorMessage: String?,
    val eventSink: (ProductDetailEvent) -> Unit,
) : CircuitUiState

sealed interface ProductDetailEvent : CircuitUiEvent {
    data object BackClicked : ProductDetailEvent
    data object Retry : ProductDetailEvent
}
