package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.staygrateful.osm.extension.showToast
import com.staygrateful.poi_test.R
import com.staygrateful.poi_test.data.models.Coordinates
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.ui.composables.SearchInputField
import com.staygrateful.poi_test.ui.navigation.Screen
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import com.staygrateful.poi_test.ui.theme.ColorContainerDark
import org.osmdroid.util.GeoPoint

@Composable
fun SearchView(
    navController: NavHostController,
    viewModels: HomeViewModel,
    onExpandChanged: (Boolean) -> Unit,
) {

    val context = LocalContext.current

    fun onSearch(query: String) {
        if (query.isEmpty()) return
        viewModels.search(query)
        navController.navigate(Screen.DetailSearchScreen.route)
    }

    Column(
        modifier = Modifier
            .background(ColorContainerDark)
            .fillMaxHeight(if(viewModels.findAreaLocation.visible) 0.5f else 0.95f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchInputField(
            margin = PaddingValues(horizontal = 20.dp),
            hint = if (viewModels.findAreaLocation.visible) {
                stringResource(R.string.find_this_area)
            } else stringResource(
                R.string.find_nearby_location
            ),
            visibleSearchCancel = viewModels.visibleSearchCancel,
            onFocusChanged = { focus ->
                onExpandChanged.invoke(focus.isFocused)
            },
            onSearch = {
                onSearch(it)
            },
            onCancel = {
                onExpandChanged.invoke(false)
            },
            onValueChange = {
                viewModels.autocompleted(
                    SearchRequest.autocompleted(
                        query = it,
                        lat = viewModels.currentGeoLocation.latitude,
                        lng = viewModels.currentGeoLocation.longitude,
                    )
                )
            }
        )
        MapAutoCompleteList(
            viewModels,
            onItemPressed = { data ->
                if(data.latitude == null || data.longitude == null) {
                    context.showToast(context.getString(R.string.not_valid_location))
                    return@MapAutoCompleteList
                }
                viewModels.updateMarkerLocation(
                    Coordinates(
                        data.main_text ?: context.getString(R.string.unknown),
                        geo = GeoPoint(data.latitude, data.longitude),
                        visible = true
                    )
                )
            }
        )
    }
}