package com.staygrateful.poi_test.data.api

import com.staygrateful.poi_test.data.models.response.AutocompleteResponse
import com.staygrateful.poi_test.data.models.response.SearchResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    /**
     * query=Hotels%20in%20San%20Francisco%2C%20USA&
     * limit=20
     * lat=37.359428
     * lng=-121.925337
     * zoom=13
     * language=en
     * region=us
     * */
    @GET(SEARCH)
    suspend fun search(
        @Query("query") query: String,
        @Query("limit") limit: Int? = null,
        @Query("lat") lat: Double? = null,
        @Query("lng") lng: Double? = null,
        @Query("zoom") zoom: Int? = null,
        @Query("language") language: String? = null,
        @Query("region") region: String? = null,
    ): Response<SearchResponse>

    /**
     * query=Hotels%20in%20San%20Francisco%2C%20USA&
     * language=en
     * region=us
     * coordinates=37.381315%2C-122.046148
     * */
    @GET(AUTO_COMPLETED)
    suspend fun autocompleted(
        @Query("query") query: String,
        @Query("language") language: String? = null,
        @Query("region") region: String? = null,
        @Query("coordinates") coordinates: String? = null,
    ): Response<AutocompleteResponse>

    companion object {
        const val SEARCH = "search"
        const val AUTO_COMPLETED = "autocomplete"
    }
}