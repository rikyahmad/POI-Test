package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.external.extension.back
import com.staygrateful.poi_test.ui.composables.SearchInputField
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import com.staygrateful.poi_test.ui.theme.ColorContainer
import com.staygrateful.poi_test.ui.theme.bottomPeekHeight
import com.staygrateful.poi_test.ui.theme.bottomSheetElevation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModels: HomeViewModel
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState(
        SheetState(
            skipHiddenState = true,
            skipPartiallyExpanded = false,
            initialValue = SheetValue.PartiallyExpanded
        )
    )
    var visibleSearchCancel by rememberSaveable { mutableStateOf(false) }

    fun expandBottomSheet(expand: Boolean) {
        scope.launch(Dispatchers.Main) {
            awaitAll(
                async {
                    visibleSearchCancel = expand
                },
                async {
                    if (expand) {
                        scaffoldState.bottomSheetState.expand()
                        return@async
                    }
                    focusManager.clearFocus()
                    scaffoldState.bottomSheetState.partialExpand()
                }
            )
        }
    }

    LaunchedEffect(scaffoldState.bottomSheetState) {
        snapshotFlow { scaffoldState.bottomSheetState.currentValue }.collect {
            if (scaffoldState.bottomSheetState.currentValue != SheetValue.Expanded) {
                expandBottomSheet(false)
            }
        }
    }

    BackHandler(
        onBack = {
            if (scaffoldState.bottomSheetState.currentValue == SheetValue.Expanded) {
                expandBottomSheet(false)
                return@BackHandler
            }
            context.back()
        }
    )

    BottomSheetScaffold(
        modifier = Modifier,
        sheetPeekHeight = bottomPeekHeight,
        scaffoldState = scaffoldState,
        sheetShadowElevation = bottomSheetElevation,
        sheetContainerColor = ColorContainer,
        sheetDragHandle = {
            Spacer(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 15.dp)
                    .height(4.dp)
                    .width(35.dp)
                    .background(Color.LightGray, RoundedCornerShape(10.dp))
            )
        },
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchInputField(
                    margin = PaddingValues(horizontal = 20.dp),
                    hint = "Search nearby location",
                    visibleSearchCancel = visibleSearchCancel,
                    onFocusChanged = { focus ->
                        if (focus.isFocused) {
                            expandBottomSheet(true)
                        }
                    },
                    onCancel = {
                        expandBottomSheet(false)
                    },
                    onValueChange = {
                        viewModels.autocompleted(
                            SearchRequest.autocompleted(
                                it,
                                lat = viewModels.currentGeoLocation.latitude,
                                lng = viewModels.currentGeoLocation.longitude,
                            )
                        )
                    }
                )
                MapAutoCompleteList(viewModels)
            }
        }
    ) {
        MapView(viewModels = viewModels)
    }
}