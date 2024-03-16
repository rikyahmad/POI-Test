package com.staygrateful.poi_test.data.models.request

data class SearchRequest(
    val query: String,
    val limit: Int? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val zoom: Int? = null,
    val language: String? = null,
    val region: String? = null,
)