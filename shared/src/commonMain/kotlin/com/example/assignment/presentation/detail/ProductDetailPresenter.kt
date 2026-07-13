package com.example.assignment.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.assignment.domain.model.Product
import com.example.assignment.domain.usecase.GetProductDetailUseCase
import com.example.assignment.presentation.navigation.ProductDetailScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter

class ProductDetailPresenter(
    private val screen: ProductDetailScreen,
    private val navigator: Navigator,
    private val getProductDetail: GetProductDetailUseCase,
) : Presenter<ProductDetailState> {

    @Composable
    override fun present(): ProductDetailState {
        var product by remember { mutableStateOf<Product?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var reloadToken by remember { mutableStateOf(0) }

        LaunchedEffect(reloadToken) {
            isLoading = true
            errorMessage = null
            getProductDetail(screen.productId)
                .onSuccess {
                    product = it
                    isLoading = false
                }
                .onFailure {
                    errorMessage = it.message ?: "Could not load product."
                    isLoading = false
                }
        }

        return ProductDetailState(
            product = product,
            isLoading = isLoading,
            errorMessage = errorMessage,
        ) { event ->
            when (event) {
                ProductDetailEvent.BackClicked -> navigator.pop()
                ProductDetailEvent.Retry -> reloadToken++
            }
        }
    }
}
