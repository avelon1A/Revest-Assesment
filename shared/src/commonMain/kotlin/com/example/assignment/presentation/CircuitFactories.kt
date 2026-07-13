package com.example.assignment.presentation

import com.example.assignment.domain.usecase.GetCategoriesUseCase
import com.example.assignment.domain.usecase.GetProductDetailUseCase
import com.example.assignment.domain.usecase.GetProductsByCategoryUseCase
import com.example.assignment.domain.usecase.GetProductsUseCase
import com.example.assignment.domain.usecase.SearchProductsUseCase
import com.example.assignment.presentation.detail.ProductDetailPresenter
import com.example.assignment.presentation.detail.ProductDetailState
import com.example.assignment.presentation.detail.ProductDetailUi
import com.example.assignment.presentation.list.ProductListPresenter
import com.example.assignment.presentation.list.ProductListState
import com.example.assignment.presentation.list.ProductListUi
import com.example.assignment.presentation.navigation.ProductDetailScreen
import com.example.assignment.presentation.navigation.ProductListScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

class AppPresenterFactory(
    private val getProducts: GetProductsUseCase,
    private val searchProducts: SearchProductsUseCase,
    private val getCategories: GetCategoriesUseCase,
    private val getProductsByCategory: GetProductsByCategoryUseCase,
    private val getProductDetail: GetProductDetailUseCase,
) : Presenter.Factory {
    override fun create(
        screen: Screen,
        navigator: Navigator,
        context: CircuitContext,
    ): Presenter<*>? = when (screen) {
        is ProductListScreen -> ProductListPresenter(
            navigator = navigator,
            getProducts = getProducts,
            searchProducts = searchProducts,
            getCategories = getCategories,
            getProductsByCategory = getProductsByCategory,
        )
        is ProductDetailScreen -> ProductDetailPresenter(
            screen = screen,
            navigator = navigator,
            getProductDetail = getProductDetail,
        )
        else -> null
    }
}

class AppUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? = when (screen) {
        is ProductListScreen -> ui<ProductListState> { state, modifier ->
            ProductListUi(state, modifier)
        }
        is ProductDetailScreen -> ui<ProductDetailState> { state, modifier ->
            ProductDetailUi(state, modifier)
        }
        else -> null
    }
}
