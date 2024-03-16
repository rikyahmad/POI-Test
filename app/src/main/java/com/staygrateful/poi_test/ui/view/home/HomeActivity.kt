package com.staygrateful.poi_test.ui.view.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.ui.theme.POITestTheme
import com.staygrateful.poi_test.ui.view.home.contract.HomepageContract
import com.staygrateful.poi_test.ui.view.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity(), HomepageContract.View {

    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel.setupLocationListener(this)

        setContent {
            POITestTheme(
                darkTheme = false // This project only support light time to saving time
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }
}