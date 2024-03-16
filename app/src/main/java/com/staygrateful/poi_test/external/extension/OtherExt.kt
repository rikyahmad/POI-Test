package com.staygrateful.poi_test.external.extension

import com.staygrateful.osm.compose.CameraState
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CameraState.resetRotation(scope: CoroutineScope, viewModels: HomeViewModel) {
    this.normalizeRotation()
    scope.launch {
        //viewModels.mapOrientation.animateTo(0f, tween(durationMillis = 1000))
    }
}