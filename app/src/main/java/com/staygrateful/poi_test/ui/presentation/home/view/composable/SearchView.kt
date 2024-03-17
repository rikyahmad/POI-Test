package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.ui.composables.SearchInputField
import com.staygrateful.poi_test.ui.navigation.Screen
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel

@Composable
fun SearchView(
    navController: NavHostController,
    viewModels: HomeViewModel,
    onExpandChanged: (Boolean) -> Unit,
) {

    val context = LocalContext.current
    fun onSearch(query: String) {
        if (query.isEmpty()) return
        navController.navigate(Screen.DetailSearchScreen.route)
        viewModels.search(
            SearchRequest.nearBy(
                query,
                lat = viewModels.currentGeoLocation.latitude,
                lng = viewModels.currentGeoLocation.longitude
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.95f)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchInputField(
            margin = PaddingValues(horizontal = 20.dp),
            hint = "Search nearby location",
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
                /*viewModels.autocompleted(
                    SearchRequest.autocompleted(
                        query = it,
                        lat = viewModels.currentGeoLocation.latitude,
                        lng = viewModels.currentGeoLocation.longitude,
                    )
                )*/
            }
        )
        MapAutoCompleteList(
            viewModels,
            onItemPressed = {

            }
        )
    }
}