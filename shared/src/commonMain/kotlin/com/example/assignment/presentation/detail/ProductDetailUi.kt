package com.example.assignment.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.assignment.domain.model.Product
import com.example.assignment.presentation.components.ProductThumbnail
import com.example.assignment.presentation.components.RatingRow
import com.example.assignment.presentation.components.formatPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailUi(
    state: ProductDetailState,
    modifier: Modifier = Modifier,
) {
    val sink = state.eventSink
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(state.product?.title ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { sink(ProductDetailEvent.BackClicked) }) {
                        Text("←")
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading ->
                    CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.errorMessage != null -> Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(state.errorMessage, style = MaterialTheme.typography.bodyLarge)
                    Button(onClick = { sink(ProductDetailEvent.Retry) }) { Text("Retry") }
                }

                state.product != null -> ProductDetailContent(state.product)
            }
        }
    }
}

@Composable
private fun ProductDetailContent(product: Product) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ProductThumbnail(
            url = product.images.firstOrNull() ?: product.thumbnail,
            contentDescription = product.title,
            modifier = Modifier.fillMaxWidth().aspectRatio(1.4f),
            cornerRadius = 16,
        )

        Text(text = product.title, style = MaterialTheme.typography.headlineSmall)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = formatPrice(product.price),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            RatingRow(rating = product.rating)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            product.brand?.takeIf { it.isNotBlank() }?.let {
                AssistChip(onClick = {}, label = { Text(it) })
            }
            if (product.category.isNotBlank()) {
                AssistChip(onClick = {}, label = { Text(product.category.replace('-', ' ')) })
            }
        }

        Text(text = "Description", style = MaterialTheme.typography.titleMedium)
        Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
    }
}
