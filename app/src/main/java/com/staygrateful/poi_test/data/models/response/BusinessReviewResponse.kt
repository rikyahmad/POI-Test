package com.staygrateful.poi_test.data.models.response

import com.google.gson.annotations.SerializedName

data class BusinessReviewResponse(
    val data: List<Data>?,
    val parameters: Parameters?,
    val request_id: String?,
    val status: String?
) {
    data class Data(
        val author_id: String?,
        val author_link: String?,
        val author_local_guide_level: Int?,
        val author_name: String?,
        val author_photo_url: String?,
        val author_review_count: Int?,
        val author_reviews_link: String?,
        val hotel_rating_breakdown: HotelRatingBreakdown?,
        val like_count: Int?,
        val owner_response_datetime_utc: Any?,
        val owner_response_language: Any?,
        val owner_response_text: Any?,
        val owner_response_timestamp: Any?,
        val rating: Int?,
        val review_datetime_utc: String?,
        val review_form: ReviewForm?,
        val review_id: String?,
        val review_language: String?,
        val review_link: String?,
        val review_photos: List<String?>?,
        val review_text: String?,
        val review_timestamp: Int?,
        val service_quality: Any?
    ) {
        data class HotelRatingBreakdown(
            val Location: Int?,
            val Rooms: Int?,
            val Service: Int?
        )

        data class ReviewForm(
            @SerializedName("What kind of trip was it?")
            val kindOfTrip: String?,
            @SerializedName("Who did you travel with?")
            val travelWith: String?
        )
    }

    data class Parameters(
        val business_id: String?,
        val language: String?,
        val limit: Int?,
        val offset: Int?,
        val query: String?,
        val region: String?,
        val sort_by: String?
    )
}