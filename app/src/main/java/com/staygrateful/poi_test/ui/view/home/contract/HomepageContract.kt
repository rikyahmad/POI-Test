package com.staygrateful.poi_test.ui.view.home.contract

import android.location.Location
import androidx.activity.ComponentActivity
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse

class HomepageContract {

    interface View {
        //fun onPermissionResult()
    }

    interface UserActionListener {

        fun setupLocationListener(activity: ComponentActivity)

        fun search(request: SearchRequest)
    }
}