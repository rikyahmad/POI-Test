package com.staygrateful.poi_test.data.models

import com.staygrateful.poi_test.data.models.response.SearchResponse

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String, data: T? = null) : NetworkResult<T>(data, message)
}