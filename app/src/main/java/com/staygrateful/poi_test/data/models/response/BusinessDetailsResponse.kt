package com.staygrateful.poi_test.data.models.response

import com.google.gson.annotations.SerializedName

data class BusinessDetailsResponse(
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
        val compound_plus_code: String?,
        val country: String?,
        val district: Any?,
        val emails_and_contacts: EmailsAndContacts?,
        val full_address: String?,
        val global_plus_code: String?,
        val google_id: String?,
        val google_mid: String?,
        val latitude: Double?,
        val longitude: Double?,
        val menu_link: Any?,
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
        val posts_link: Any?,
        val posts_sample: Any?,
        val price_level: Any?,
        val rating: Double?,
        val reservations_link: Any?,
        val review_count: Int?,
        val reviews_link: String?,
        val reviews_per_rating: ReviewsPerRating?,
        val reviews_sample: List<ReviewsSample?>?,
        val state: String?,
        val street_address: String?,
        val subtypes: List<String?>?,
        val timezone: String?,
        val type: String?,
        val verified: Boolean?,
        val website: String?,
        val working_hours: WorkingHours?,
        val zipcode: String?
    ) {
        data class About(
            val details: Details?,
            val summary: Any?
        ) {
            data class Details(
                @SerializedName("Accessibility")
                val accessibility: Accessibility?
            ) {
                data class Accessibility(
                    @SerializedName("Wheelchair accessible entrance")
                    val accessibleEntrance: Boolean?,
                    @SerializedName("Wheelchair accessible parking lot")
                    val accessibleParkingLot: Boolean?
                )
            }
        }

        data class EmailsAndContacts(
            val emails: List<String?>?,
            val facebook: String?,
            val github: Any?,
            val instagram: String?,
            val linkedin: String?,
            val phone_numbers: List<String?>?,
            val pinterest: Any?,
            val snapchat: Any?,
            val tiktok: Any?,
            val twitter: String?,
            val yelp: String?,
            val youtube: Any?
        )

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
            val `1`: Int?,
            val `2`: Int?,
            val `3`: Int?,
            val `4`: Int?,
            val `5`: Int?
        )

        data class ReviewsSample(
            val author_id: String?,
            val author_link: String?,
            val author_local_guide_level: Int?,
            val author_name: String?,
            val author_photo_url: String?,
            val author_review_count: Int?,
            val author_reviews_link: String?,
            val hotel_rating_breakdown: Any?,
            val like_count: Int?,
            val owner_response_datetime_utc: Any?,
            val owner_response_language: Any?,
            val owner_response_text: Any?,
            val owner_response_timestamp: Any?,
            val rating: Int?,
            val review_datetime_utc: String?,
            val review_form: Any?,
            val review_id: String?,
            val review_language: String?,
            val review_link: String?,
            val review_photos: Any?,
            val review_text: String?,
            val review_timestamp: Int?,
            val service_quality: ServiceQuality?
        ) {
            data class ServiceQuality(
                val Professionalism: String?,
                val Quality: String?
            )
        }

        data class WorkingHours(
            val Friday: List<String?>?,
            val Monday: List<String?>?,
            val Saturday: List<String?>?,
            val Sunday: List<String?>?,
            val Thursday: List<String?>?,
            val Tuesday: List<String?>?,
            val Wednesday: List<String?>?
        )
    }

    data class Parameters(
        val business_id: String?,
        val coordinates: String?,
        val extract_emails_and_contacts: Boolean?,
        val extract_share_link: Boolean?,
        val language: String?,
        val region: String?
    )
}