package com.staygrateful.poi_test.data.repository

import com.staygrateful.poi_test.data.models.BaseApiResponse
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.AutocompleteResponse
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
                    query = request.query,
                    limit = request.limit,
                    lat = request.lat,
                    lng = request.lng,
                    zoom = request.zoom,
                    language = request.language,
                    region = request.region
                )
            })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun autocompleted(
        request: SearchRequest
    ): Flow<NetworkResult<AutocompleteResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.autocompleted(
                    query = request.query,
                    language = request.language,
                    region = request.region,
                    coordinates = request.coordinates,
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}