package com.staygrateful.poi_test.data.models.request

class BusinessRequest private constructor(
    val business_id: String,
    val limit: Int? = null,
    val language: String? = null,
    val region: String? = null,
    val coordinates: String? = null,
    val extractEmailsContacts: Boolean? = null,
) {
    companion object {

        fun details(
            business_id: String,
            limit: Int? = null,
            language: String? = null,
            region: String? = null,
            coordinates: String? = null,
            extractEmailsContacts: Boolean? = null,
        ): BusinessRequest {
            return BusinessRequest(
                business_id, limit,  language, region, coordinates, extractEmailsContacts
            )
        }

        fun photos(
            business_id: String,
            limit: Int? = null,
            language: String? = null,
            region: String? = null,
        ): BusinessRequest {
            return BusinessRequest(
                business_id, limit,  language, region
            )
        }

        fun reviews(
            business_id: String,
            limit: Int? = null,
            language: String? = null,
            region: String? = null,
        ): BusinessRequest {
            return BusinessRequest(
                business_id, limit,  language, region
            )
        }
    }
}