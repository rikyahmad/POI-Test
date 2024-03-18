package com.staygrateful.poi_test.domain.usecase

import android.media.tv.SectionRequest
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.BusinessRequest
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.AutocompleteResponse
import com.staygrateful.poi_test.data.models.response.BusinessDetailsResponse
import com.staygrateful.poi_test.data.models.response.BusinessPhotoResponse
import com.staygrateful.poi_test.data.models.response.BusinessReviewResponse
import com.staygrateful.poi_test.data.models.response.SearchResponse
import kotlinx.coroutines.flow.Flow

interface HomepageUseCase {

    suspend fun search(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>>

    suspend fun searchNearby(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>>

    suspend fun searchInArea(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>>

    suspend fun autocompleted(
        request: SearchRequest
    ): Flow<NetworkResult<AutocompleteResponse>>

    suspend fun businessDetails(
        request: BusinessRequest
    ): Flow<NetworkResult<BusinessDetailsResponse>>

    suspend fun businessPhotos(
        request: BusinessRequest
    ): Flow<NetworkResult<BusinessPhotoResponse>>

    suspend fun businessReviews(
        request: BusinessRequest
    ): Flow<NetworkResult<BusinessReviewResponse>>
}