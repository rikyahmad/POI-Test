package com.staygrateful.poi_test.ui.view.home.contract

import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse

class HomepageContract {

    interface View {
        //fun onPermissionResult()
    }

    interface UserActionListener {
        fun search(request: SearchRequest)
    }
}