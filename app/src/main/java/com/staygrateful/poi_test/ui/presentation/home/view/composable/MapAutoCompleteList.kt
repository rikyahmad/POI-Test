package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import com.staygrateful.poi_test.ui.theme.ColorContainerDark
import com.staygrateful.poi_test.ui.theme.ColorDivider

@Composable
fun ColumnScope.MapAutoCompleteList(viewModels: HomeViewModel) {

    val lazyListState = rememberLazyListState()
    val responseItems by viewModels.autocompletedResponse.observeAsState()

    if (responseItems.isNullOrEmpty()) {
        return
    }

    Spacer(
        modifier = Modifier
            .padding(top = 20.dp)
            .height(1.dp)
            .fillMaxWidth()
            .background(ColorDivider)
    )

    LazyColumn(
        modifier = Modifier.weight(1f),
        state = lazyListState,

        ) {
        itemsIndexed(responseItems!!) { index, data ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {

                    }
                    .padding(horizontal = 20.dp),
            ) {
                if (index > 0) {
                    Spacer(
                        modifier = Modifier
                            .padding(start = 50.dp)
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(ColorDivider)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        "Test",
                        modifier = Modifier
                            .background(ColorContainerDark, CircleShape)
                            .padding(7.dp)
                            .size(21.dp),
                        tint = Color.Black
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 15.dp)
                            .padding(vertical = 12.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = data.main_text ?: "-",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = data.description ?: "-",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.Black.copy(0.7f)
                        )
                    }
                }
            }
        }
    }
}