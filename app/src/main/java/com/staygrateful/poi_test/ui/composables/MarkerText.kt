package com.staygrateful.poi_test.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkerText(
    onClick: () -> Unit = {},
    title: String,
    fontSize: TextUnit = 13.sp,
    color: Color = Color.White,
    contentColor: Color = Color.Red,
    fontWeight: FontWeight = FontWeight.SemiBold,
) {
    CustomButton(
        onClick = onClick,
        elevation = 3.dp,
        borderWidth = 0.dp,
        borderColor = Color.Transparent,
        contentColor = contentColor,
        color = color,
        corner = 5.dp,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 1.dp),
        modifier = Modifier
    ) {
        Text(text = title, fontSize = fontSize, fontWeight = fontWeight)
    }
}