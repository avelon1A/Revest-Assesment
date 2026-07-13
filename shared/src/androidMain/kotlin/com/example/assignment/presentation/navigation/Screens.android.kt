package com.example.assignment.presentation.navigation

import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
actual object ProductListScreen : Screen

@Parcelize
actual data class ProductDetailScreen actual constructor(actual val productId: Int) : Screen
