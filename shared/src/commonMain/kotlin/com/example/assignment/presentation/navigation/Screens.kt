package com.example.assignment.presentation.navigation

import com.slack.circuit.runtime.screen.Screen

expect object ProductListScreen : Screen

expect class ProductDetailScreen(productId: Int) : Screen {
    val productId: Int
}
