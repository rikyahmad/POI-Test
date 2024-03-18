package com.staygrateful.poi_test.data.models.response

data class BusinessPhotoResponse(
    val cursor: String?,
    val data: List<Data>?,
    val parameters: Parameters?,
    val request_id: String?,
    val status: String?
) {
    data class Data(
        val latitude: Double?,
        val longitude: Double?,
        val photo_datetime_utc: String?,
        val photo_id: String?,
        val photo_timestamp: Int?,
        val photo_url: String?,
        val photo_url_large: String?,
        val type: String?,
        val video_thumbnail_url: Any?
    )

    data class Parameters(
        val business_id: String?,
        val language: String?,
        val limit: Int?,
        val region: String?
    )
}