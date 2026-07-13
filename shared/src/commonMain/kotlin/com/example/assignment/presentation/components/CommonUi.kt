package com.example.assignment.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlin.math.roundToInt

fun formatPrice(price: Double): String {
    val cents = (price * 100).roundToInt()
    val whole = cents / 100
    val fraction = (cents % 100).toString().padStart(2, '0')
    return "$$whole.$fraction"
}

@Composable
fun ProductThumbnail(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    cornerRadius: Int = 12,
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(RoundedCornerShape(cornerRadius.dp)),
    )
}

@Composable
fun RatingRow(
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = "★",
            color = Color(0xFFFFB300),
            style = MaterialTheme.typography.labelMedium,
        )
        Text(
            text = ((rating * 10).roundToInt() / 10.0).toString(),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
