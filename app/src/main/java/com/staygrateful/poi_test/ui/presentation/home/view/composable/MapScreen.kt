package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.staygrateful.poi_test.external.extension.back
import com.staygrateful.poi_test.ui.navigation.Screen
import com.staygrateful.poi_test.ui.navigation.SearchNavGraph
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
    val navController = rememberNavController()
    val searchSheetState = rememberBottomSheetScaffoldState(
        SheetState(
            skipHiddenState = true,
            skipPartiallyExpanded = false,
            initialValue = SheetValue.PartiallyExpanded
        )
    )

    fun expandSearchBottomSheet(expand: Boolean) {
        scope.launch(Dispatchers.Main) {
            if (expand) {
                searchSheetState.bottomSheetState.expand()
                return@launch
            }
            focusManager.clearFocus()
            searchSheetState.bottomSheetState.partialExpand()
        }
    }

    LaunchedEffect(searchSheetState.bottomSheetState) {
        snapshotFlow { searchSheetState.bottomSheetState.currentValue }.collect {
            viewModels.visibleSearchCancel = it == SheetValue.Expanded
        }
    }

    BackHandler(
        onBack = {
            if (searchSheetState.bottomSheetState.currentValue != SheetValue.PartiallyExpanded) {
                expandSearchBottomSheet(false)
                return@BackHandler
            }
            context.back()
        }
    )

    @Composable
    fun dragHandle() {
        Spacer(
            modifier = Modifier
                .padding(top = 10.dp, bottom = 15.dp)
                .height(4.dp)
                .width(35.dp)
                .background(Color.LightGray, RoundedCornerShape(10.dp))
        )
    }

    BottomSheetScaffold(
        modifier = Modifier,
        sheetPeekHeight = bottomPeekHeight,
        scaffoldState = searchSheetState,
        sheetShadowElevation = bottomSheetElevation,
        sheetContainerColor = ColorContainer,
        sheetDragHandle = {
            dragHandle()
        },
        sheetContent = {
            SearchNavGraph(
                navController = navController,
                viewModels,
                onExpandChanged = {
                    expandSearchBottomSheet(it)
                }
            )
            /*Button(onClick = {
                navController.navigate(Screen.DetailSearchScreen.route)
            }) {
                Text(text = "Next")
            }*/
        },
        content = {
            MapView(viewModels = viewModels)
        }
    )
}