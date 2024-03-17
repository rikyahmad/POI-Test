package com.staygrateful.poi_test.ui.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    enabled: Boolean = true,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    color: Color = Color.White,
    contentColor: Color = Color.Red,
    elevation: Dp = 0.dp,
    corner: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 5.dp, vertical = 5.dp), //ButtonDefaults.ContentPadding
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            disabledElevation = elevation
        ),
        contentPadding = contentPadding,
        border = BorderStroke(borderWidth, borderColor),
        shape = shape ?: RoundedCornerShape(corner), // = 50% percent
        // or shape = CircleShape
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            containerColor = color
        )
    ) {
        content()
    }
}