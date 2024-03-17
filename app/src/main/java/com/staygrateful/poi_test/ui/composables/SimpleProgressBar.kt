package com.staygrateful.poi_test.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.staygrateful.poi_test.ui.theme.ColorContainerDark
import com.staygrateful.poi_test.ui.theme.ColorPrimary

@Composable
fun SimpleProgressBar(
    text: String,
    showProgress: Boolean
) {
    AnimatedVisibility(visible = showProgress) {
        Column(
            modifier = Modifier
                .padding(vertical = 35.dp, horizontal = 20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .size(24.dp),
                color = ColorPrimary,
                trackColor = ColorContainerDark
            )
            Text(
                text = text,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
    }
}