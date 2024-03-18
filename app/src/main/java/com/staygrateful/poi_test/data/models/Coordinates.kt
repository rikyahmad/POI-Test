package com.staygrateful.poi_test.data.models

import android.os.Parcelable
import com.staygrateful.poi_test.Config
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.osmdroid.util.GeoPoint

@Parcelize
data class Coordinates(
    val name: String,
    val snipet: String = "",
    val visible: Boolean = false,
    var offsetX: Float = 0f,
    var zoomLevel: Double? = null,
    val geo: GeoPoint
) : Parcelable {

    @IgnoredOnParcel
    val id: String = "${geo.latitude}, ${geo.longitude}"

    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {

        val Initial: Coordinates = Coordinates(
            "Pin Location", geo = InitialGeoPoint
        )

        fun fromGeo(geo: GeoPoint): Coordinates {
            return Coordinates("Location", geo = geo)
        }
    }
}

val InitialGeoPoint: GeoPoint = GeoPoint(0.0, 0.0)