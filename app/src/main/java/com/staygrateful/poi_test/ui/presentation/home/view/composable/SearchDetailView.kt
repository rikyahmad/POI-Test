package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.staygrateful.poi_test.R
import com.staygrateful.poi_test.ui.composables.SimpleProgressBar
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import com.staygrateful.poi_test.ui.theme.ColorClosed
import com.staygrateful.poi_test.ui.theme.ColorContainerDark
import com.staygrateful.poi_test.ui.theme.ColorDivider
import com.staygrateful.poi_test.ui.theme.ColorDividerDark
import com.staygrateful.poi_test.ui.theme.ColorOpen
import com.staygrateful.poi_test.ui.theme.ColorRating
import java.util.Locale


@Composable
fun SearchDetailView(
    navController: NavHostController,
    viewModels: HomeViewModel,
    onExpandChanged: (Boolean) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    val responseItems by viewModels.searchResponse.observeAsState()

    if (responseItems?.data?.data.isNullOrEmpty()) {
        if (viewModels.searchLoading) {
            SimpleProgressBar(
                text = "Searching...",
                showProgress = viewModels.searchLoading
            )
            return
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Result Not Found",
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
        }
        return
    }

    onExpandChanged.invoke(true)

    Column(
        modifier = Modifier.background(ColorContainerDark)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                "Test",
                modifier = Modifier
                    .background(ColorDividerDark, CircleShape)
                    .padding(7.dp)
                    .size(24.dp),
                tint = Color.Black
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp, end = 15.dp)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "\"${responseItems?.data?.parameters?.query}\"",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(
                        R.string.search_found,
                        responseItems?.data?.data?.size ?: 0
                    ),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black.copy(0.7f)
                )
            }

            Icon(
                imageVector = Icons.Default.Clear,
                "Close",
                modifier = Modifier
                    //.align(Alignment.Top)
                    .background(ColorContainerDark, CircleShape)
                    .clip(CircleShape)
                    .clickable {
                        navController.popBackStack()
                    }
                    .padding(5.dp)
                    .size(18.dp),
                tint = Color.Black
            )
        }

        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(ColorDividerDark)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = lazyListState,

            ) {
            itemsIndexed(responseItems?.data?.data!!) { index, data ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModels.navigateToLocationDetails(navController, data)
                        }
                        .padding(horizontal = 20.dp),
                ) {
                    if (index > 0) {
                        Spacer(
                            modifier = Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(ColorDividerDark)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 15.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = data.name ?: "-",
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                modifier = Modifier.padding(top = 2.dp),
                                text = data.full_address ?: "-",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                maxLines = 2,
                                lineHeight = 17.sp,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Black.copy(0.7f)
                            )
                            Row(
                                modifier = Modifier.padding(top = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(1.dp)
                            ) {
                                if (data.rating != null) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        "Rating",
                                        modifier = Modifier
                                            .size(19.dp),
                                        tint = ColorRating
                                    )
                                    Text(
                                        modifier = Modifier.padding(end = 5.dp),
                                        text = data.rating.toString(),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = ColorRating
                                    )
                                }
                                Text(
                                    text = "${data.type} • ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.Black.copy(0.7f)
                                )
                                Text(
                                    modifier = Modifier.padding(start = 0.dp),
                                    text = data.status,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (data.isOpen) ColorOpen else ColorClosed
                                )
                            }
                        }
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(data.photos_sample?.first()?.photo_url)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .padding(start = 15.dp)
                                .size(60.dp)
                                .clip(RoundedCornerShape(5.dp))
                                .background(Color.LightGray)
                        )
                    }
                }
            }
        }
    }
}