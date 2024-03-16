package com.staygrateful.poi_test.ui.presentation.home.contract

import androidx.activity.ComponentActivity
import com.staygrateful.poi_test.data.models.request.SearchRequest

class HomepageContract {

    interface View {
        //fun onPermissionResult()
    }

    interface UserActionListener {

        fun setupLocationListener(activity: ComponentActivity)

        fun search(request: SearchRequest)
    }
}