package com.staygrateful.poi_test.ui.presentation.home.contract

import androidx.activity.ComponentActivity
import androidx.navigation.NavHostController
import com.staygrateful.poi_test.data.models.request.BusinessRequest
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse

class HomepageContract {

    interface View {
        //fun onPermissionResult()
    }

    interface UserActionListener {

        fun setupLocationListener(activity: ComponentActivity)

        fun search(request: SearchRequest)

        fun autocompleted(request: SearchRequest)

        fun businessDetails(request: BusinessRequest)

        fun navigateToLocationDetails(navController: NavHostController, data: SearchResponse.Data)
    }
}