package com.example.assignment.presentation.navigation

import com.slack.circuit.runtime.screen.Screen

actual object ProductListScreen : Screen

actual data class ProductDetailScreen actual constructor(actual val productId: Int) : Screen
