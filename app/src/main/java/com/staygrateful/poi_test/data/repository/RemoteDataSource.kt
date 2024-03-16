package com.staygrateful.poi_test.data.repository

import com.staygrateful.poi_test.data.api.ApiService
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
): ApiService {

    override suspend fun search(
        query: String,
        limit: Int?,
        lat: Double?,
        lng: Double?,
        zoom: Int?,
        language: String?,
        region: String?
    ): Response<SearchResponse> {
        return apiService.search(query, limit, lat, lng, zoom, language, region)
    }

}