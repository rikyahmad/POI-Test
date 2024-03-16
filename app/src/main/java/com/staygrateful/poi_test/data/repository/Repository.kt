package com.staygrateful.poi_test.data.repository

import com.staygrateful.poi_test.data.models.BaseApiResponse
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun search(
        request: SearchRequest
    ): Flow<NetworkResult<SearchResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.search(
                    request.query,
                    request.limit,
                    request.lat,
                    request.lng,
                    request.zoom,
                    request.language,
                    request.region
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}