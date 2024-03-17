package com.staygrateful.poi_test.data.repository

import com.staygrateful.poi_test.data.api.ApiService
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.AutocompleteResponse
import com.staygrateful.poi_test.data.models.response.BusinessDetailsResponse
import com.staygrateful.poi_test.data.models.response.SearchResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
): ApiService {
    override suspend fun searchNearby(
        query: String,
        limit: Int?,
        lat: Double?,
        lng: Double?,
        zoom: Int?,
        language: String?,
        region: String?
    ): Response<SearchResponse> {
        return apiService.searchNearby(query, limit, lat, lng, zoom, language, region)
    }

    override suspend fun searchInArea(
        query: String,
        limit: Int?,
        lat: Double?,
        lng: Double?,
        zoom: Int?,
        language: String?,
        region: String?
    ): Response<SearchResponse> {
        return apiService.searchInArea(query, limit, lat, lng, zoom, language, region)
    }

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

    override suspend fun autocompleted(
        query: String,
        language: String?,
        region: String?,
        coordinates: String?
    ): Response<AutocompleteResponse> {
        return apiService.autocompleted(query, language, region, coordinates)
    }

    override suspend fun businessDetails(
        businessId: String,
        language: String?,
        region: String?,
        extractEmailsContacts: Boolean?,
        coordinates: String?
    ): Response<BusinessDetailsResponse> {
        return apiService.businessDetails(businessId, language, region, extractEmailsContacts, coordinates)
    }

}