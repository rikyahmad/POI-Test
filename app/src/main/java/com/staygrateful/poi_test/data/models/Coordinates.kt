package com.staygrateful.poi_test.data.models

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.osmdroid.util.GeoPoint

@Parcelize
data class Coordinates(
    val name: String,
    val address: String,
    var offsetX: Float = 0f,
    val geo: GeoPoint
): Parcelable {

    @IgnoredOnParcel
    val id: String = "${geo.latitude}, ${geo.longitude}"

    companion object {

    }
}

val dummyCoordinatesList = listOf(
    Coordinates(
        name = "Cheri",
        address = "Jalan Mendawai I no 41, Kebayoran Baru Kec. Kebayoran Baru, Kota Jakarta Selatan DKI Jakarta 12150",
        geo = GeoPoint(-7.408225396033306, 110.941495748359)
    ),
    Coordinates(
        name = "Nindita",
        address = "Jl RawaDomba No 16A Kec. Duren Sawit, Kota Jakarta Timur DKI Jakarta 13440",
        geo = GeoPoint(-7.414363212037654, 110.94009943414252)
    ),
    Coordinates(
        name = "Mega",
        address = "Apartemen Paladian Park, Tower A unit 105 Kelapagading Kec. Kelapa Gading, Kota Jakarta Utara DKI Jakarta 14240",
        geo = GeoPoint(-7.417290199122309, 110.9542498613663)
    ),
)