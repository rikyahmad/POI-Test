package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.staygrateful.poi_test.ui.composables.RatingBar
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import com.staygrateful.poi_test.ui.theme.ColorClosed
import com.staygrateful.poi_test.ui.theme.ColorContainerDark
import com.staygrateful.poi_test.ui.theme.ColorDivider
import com.staygrateful.poi_test.ui.theme.ColorDividerDark
import com.staygrateful.poi_test.ui.theme.ColorOpen
import com.staygrateful.poi_test.ui.theme.ColorRating


@Composable
fun LocationDetailView(
    navController: NavHostController,
    viewModels: HomeViewModel,
    onExpandChanged: (Boolean) -> Unit,
) {
    val scrollState = rememberScrollState()
    val photoListState = rememberLazyListState()
    val reviewListState = rememberLazyListState()
    val businessDetailsResponse by viewModels.businessDetailsResponse.observeAsState()

    val details = viewModels.locationDetailSelected
    val businessDetails = businessDetailsResponse?.firstOrNull()

    if (details == null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center),
                text = "Invalid Data",
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 15.dp)
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = details.name ?: "-",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = details.type ?: "-",
                    fontSize = 13.sp,
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
                .padding(horizontal = 20.dp)
                .height(1.dp)
                .fillMaxWidth()
                .background(ColorDividerDark)
        )

        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleInfo(title = "Hours") {
                    Text(
                        text = details.status,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (details.isOpen) ColorOpen else ColorClosed
                    )
                }
                TitleInfo(title = "Verified") {
                    Text(
                        text = if (details.verified == true) "Yes" else "No",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = if (details.verified == true) ColorOpen else Color.Black
                    )
                }
                TitleInfo(title = "Review") {
                    Text(
                        text = details.review_count?.toString() ?: "0",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Black
                    )
                }
                TitleInfo(title = "Rating") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            "Rating",
                            modifier = Modifier
                                .size(19.dp),
                            tint = ColorRating
                        )
                        Text(
                            modifier = Modifier.padding(end = 5.dp),
                            text = details.rating?.toString() ?: "0",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = ColorRating
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(ColorDividerDark)
            )

            if (businessDetails?.photos_sample != null) {
                LazyRow(
                    modifier = Modifier.height(250.dp),
                    state = photoListState,
                    contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(businessDetails.photos_sample) { data ->
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(data?.photo_url)
                                .crossfade(true)
                                .build(),
                            contentDescription = "${data?.photo_id}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .shadow(1.dp, RoundedCornerShape(15.dp))
                                .aspectRatio(0.75f)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable {

                                }
                                .background(Color.White, RoundedCornerShape(15.dp))
                        )
                    }
                }
            }

            if (businessDetails?.reviews_sample != null) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 10.dp, top = 20.dp),
                    text = "Ratings & Reviews",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                LazyRow(
                    modifier = Modifier.height(155.dp),
                    state = reviewListState,
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 0.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(businessDetails.reviews_sample) { data ->
                        Column(
                            modifier = Modifier
                                .shadow(1.dp, RoundedCornerShape(15.dp))
                                .aspectRatio(1.8f)
                                .clip(RoundedCornerShape(15.dp))
                                .clickable {

                                }
                                .background(Color.White, RoundedCornerShape(15.dp))
                                .padding(15.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier,
                                text = data?.review_text ?: "-",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Normal,
                                maxLines = 3,
                                lineHeight = 17.sp,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.Black.copy(0.7f)
                            )
                            Row(
                                modifier = Modifier
                                    .height(35.dp)
                                    .padding(top = 0.dp),
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(data?.author_photo_url)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Author",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .clip(CircleShape)
                                        .background(Color.LightGray, CircleShape)
                                )
                                Column(
                                    modifier = Modifier.padding(start = 7.dp),
                                    verticalArrangement = Arrangement.spacedBy(1.dp)
                                ) {
                                    RatingBar(
                                        rating = data?.rating?.toDouble() ?: 0.0,
                                        size = 15.dp,
                                        space = 2.dp
                                    )

                                    Text(
                                        modifier = Modifier,
                                        text = data?.author_name ?: "-",
                                        fontSize = 11.sp,
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

            Text(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 0.dp, top = 20.dp),
                text = "Details",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 15.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(15.dp))
                    .padding(15.dp)
            ) {
                ValueInfo(
                    title = "Phone",
                    value = details.phone_number ?: businessDetails?.phone_number ?: "-"
                )
                ValueInfo(
                    title = "Address",
                    value = details.full_address ?: businessDetails?.full_address ?: "-"
                )
                ValueInfo(
                    title = "Website",
                    value = details.website ?: businessDetails?.website ?: "-",
                    false
                )
            }
        }
    }
}

@Composable
fun RowScope.TitleInfo(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black.copy(0.9f)
        )
        content()
    }
}

@Composable
fun ColumnScope.ValueInfo(title: String, value: String, showSpacer: Boolean = true) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black.copy(0.7f)
        )
        Text(
            text = value,
            fontSize = 13.5.sp,
            fontWeight = FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color.Black.copy(0.9f)
        )
        if (showSpacer) {
            Spacer(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(ColorDivider)
            )
        }
    }
}