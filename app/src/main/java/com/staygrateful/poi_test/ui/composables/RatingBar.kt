package com.staygrateful.poi_test.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.staygrateful.poi_test.ui.theme.ColorRating


@Composable
fun RatingBar(rating: Double, size: Dp = 19.dp, space: Dp = 3.dp) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space)
    ) {
        for (i in 1..5) {
            Icon(
                imageVector = Icons.Default.Star,
                "Rating",
                modifier = Modifier
                    .size(size),
                tint = if(i <= rating) ColorRating else Color.LightGray
            )
        }
    }
}

@Preview
@Composable
fun RatingBarPreview() {
    RatingBar(rating = 3.5)
}