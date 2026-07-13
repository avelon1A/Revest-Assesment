package com.example.assignment.di

import com.example.assignment.data.remote.ProductApi
import com.example.assignment.data.remote.createHttpClient
import com.example.assignment.data.repository.ProductRepositoryImpl
import com.example.assignment.domain.repository.ProductRepository
import com.example.assignment.domain.usecase.GetCategoriesUseCase
import com.example.assignment.domain.usecase.GetProductDetailUseCase
import com.example.assignment.domain.usecase.GetProductsByCategoryUseCase
import com.example.assignment.domain.usecase.GetProductsUseCase
import com.example.assignment.domain.usecase.SearchProductsUseCase
import com.example.assignment.presentation.AppPresenterFactory
import com.example.assignment.presentation.AppUiFactory
import com.slack.circuit.foundation.Circuit
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient() }
    single { ProductApi(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }

    factory { GetProductsUseCase(get()) }
    factory { SearchProductsUseCase(get()) }
    factory { GetProductDetailUseCase(get()) }
    factory { GetCategoriesUseCase(get()) }
    factory { GetProductsByCategoryUseCase(get()) }

    single { AppUiFactory() }
    single {
        AppPresenterFactory(
            getProducts = get(),
            searchProducts = get(),
            getCategories = get(),
            getProductsByCategory = get(),
            getProductDetail = get(),
        )
    }
    single {
        Circuit.Builder()
            .addUiFactory(get<AppUiFactory>())
            .addPresenterFactory(get<AppPresenterFactory>())
            .build()
    }
}
