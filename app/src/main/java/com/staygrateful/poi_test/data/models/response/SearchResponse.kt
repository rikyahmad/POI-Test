package com.staygrateful.poi_test.data.models.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(

    val data: List<Data>?,
    val parameters: Parameters?,
    val request_id: String?,
    val status: String?
) {
    data class Data(
        val about: About?,
        val address: String?,
        val booking_link: Any?,
        val business_id: String?,
        val business_status: String?,
        val cid: String?,
        val city: String?,
        val country: String?,
        val district: String?,
        val full_address: String?,
        val google_id: String?,
        val hotel_amenities: HotelAmenities?,
        val hotel_location_rating: HotelLocationRating?,
        val hotel_review_summary: HotelReviewSummary?,
        val hotel_stars: Int?,
        val latitude: Double?,
        val longitude: Double?,
        val name: String?,
        val order_link: Any?,
        val owner_id: String?,
        val owner_link: String?,
        val owner_name: String?,
        val phone_number: String?,
        val photo_count: Int?,
        val photos_sample: List<PhotosSample?>?,
        val place_id: String?,
        val place_link: String?,
        val price_level: Any?,
        val rating: Double?,
        val reservations_link: Any?,
        val review_count: Int?,
        val reviews_link: String?,
        val reviews_per_rating: ReviewsPerRating?,
        val state: String?,
        val street_address: String?,
        val subtypes: List<String?>?,
        val timezone: String?,
        val type: String?,
        val verified: Boolean?,
        val website: String?,
        val working_hours: Any?,
        val zipcode: String?
    ) {
        data class About(
            val details: Any?,
            val summary: String?
        )

        data class HotelAmenities(
            @SerializedName("Free Wi-Fi")
            val freeWifi: Boolean?,
            @SerializedName("Free breakfast")
            val freeBreakfast: Boolean?
        )

        data class HotelLocationRating(
            @SerializedName("Airports")
            val airports: Double?,
            @SerializedName("Overall")
            val overall: Double?,
            @SerializedName("Things to do")
            val thingsToDo: Double?,
            @SerializedName("Transit")
            val transit: Double?
        )

        data class HotelReviewSummary(
            @SerializedName("Location")
            val location: Location?,
            @SerializedName("Rooms")
            val rooms: Rooms?,
            @SerializedName("Service & facilities")
            val servicesAndFacilities: ServiceFacilities?
        ) {
            data class Location(
                val score: Double?,
                val summary: List<String?>?
            )

            data class Rooms(
                val score: Double?,
                val summary: List<String?>?
            )

            data class ServiceFacilities(
                val score: Double?,
                val summary: List<String?>?
            )
        }

        data class PhotosSample(
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

        data class ReviewsPerRating(
            @SerializedName("1")
            val veryBad: Int?,
            @SerializedName("2")
            val bad: Int?,
            @SerializedName("3")
            val neutral: Int?,
            @SerializedName("4")
            val good: Int?,
            @SerializedName("5")
            val veryGood: Int?
        )
    }

    data class Parameters(
        val language: String?,
        val lat: Double?,
        val limit: Int?,
        val lng: Double?,
        val query: String?,
        val region: String?,
        val zoom: Int?
    )
}