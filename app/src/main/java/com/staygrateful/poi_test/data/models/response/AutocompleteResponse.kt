package com.staygrateful.poi_test.data.models.response

data class AutocompleteResponse(
    val data: List<Data>?,
    val request_id: String?,
    val status: String?
) {
    data class Data(
        val country: String?,
        val description: String?,
        val google_id: Any?,
        val latitude: Double?,
        val longitude: Double?,
        val main_text: String?,
        val main_text_highlights: List<MainTextHighlight>?,
        val secondary_text: String?,
        val secondary_text_highlights: List<SecondaryTextHighlight>?,
        val type: String?
    ) {
        data class MainTextHighlight(
            val length: Int?,
            val offset: Int?
        )

        data class SecondaryTextHighlight(
            val length: Int?,
            val offset: Int?
        )
    }
}