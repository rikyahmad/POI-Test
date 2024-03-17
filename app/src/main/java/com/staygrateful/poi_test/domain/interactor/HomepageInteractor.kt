package com.staygrateful.poi_test.domain.interactor

import com.staygrateful.poi_test.data.models.BaseApiResponse
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.BusinessRequest
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.AutocompleteResponse
import com.staygrateful.poi_test.data.models.response.BusinessDetailsResponse
import com.staygrateful.poi_test.data.models.response.SearchResponse
import com.staygrateful.poi_test.data.repository.Repository
import com.staygrateful.poi_test.domain.usecase.HomepageUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomepageInteractor @Inject constructor(
    private val repository: Repository
) : HomepageUseCase, BaseApiResponse() {

    override suspend fun search(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>> {
        return repository.search(request)
    }

    override suspend fun searchNearby(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>> {
        return repository.searchNearby(request)
    }

    override suspend fun searchInArea(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>> {
        return repository.searchInArea(request)
    }

    override suspend fun autocompleted(
        request: SearchRequest
    ): Flow<NetworkResult<AutocompleteResponse>> {
        return repository.autocompleted(request)
    }

    override suspend fun businessDetails(
        request: BusinessRequest
    ): Flow<NetworkResult<BusinessDetailsResponse>> {
        return repository.businessDetails(request)
    }
}