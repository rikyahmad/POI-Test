package com.staygrateful.poi_test.data.models.request

class SearchRequest private constructor(
    val query: String,
    val limit: Int? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val zoom: Int? = null,
    val language: String? = null,
    val region: String? = null,
) {

    companion object {

        fun inArea(
            query: String,
            lat: Double,
            lng: Double,
            zoom: Int,
            limit: Int? = null,
            language: String? = null,
            region: String? = null,
        ): SearchRequest {
            return SearchRequest(
                query, limit, lat, lng, zoom, language, region
            )
        }

        fun nearBy(
            query: String,
            lat: Double,
            lng: Double,
            limit: Int? = null,
            language: String? = null,
            region: String? = null,
        ): SearchRequest {
            return SearchRequest(
                query, limit, lat, lng, null, language, region
            )
        }
    }
}