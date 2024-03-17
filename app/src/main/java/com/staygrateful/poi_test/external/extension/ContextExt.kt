package com.staygrateful.poi_test.external.extension

import android.app.Activity
import android.content.Context
import com.staygrateful.osm.compose.CameraState
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Context.back() {
    if(this is Activity) {
        this.finish()
    }
}