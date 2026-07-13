package com.example.assignment

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import com.example.assignment.presentation.navigation.ProductListScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import org.koin.compose.koinInject

@Composable
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory()) }
            .crossfade(true)
            .build()
    }

    val circuit = koinInject<Circuit>()
    MaterialTheme {
        val backStack = rememberSaveableBackStack(root = ProductListScreen)
        val navigator = rememberCircuitNavigator(backStack) { }
        CircuitCompositionLocals(circuit) {
            NavigableCircuitContent(
                navigator = navigator,
                backStack = backStack,
            )
        }
    }
}
