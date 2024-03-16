package com.staygrateful.poi_test.domain.usecase

import android.media.tv.SectionRequest
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse
import kotlinx.coroutines.flow.Flow

interface HomepageUseCase {

    suspend fun search(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>>
}