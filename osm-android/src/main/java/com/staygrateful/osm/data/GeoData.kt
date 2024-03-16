package com.staygrateful.osm.data

import org.osmdroid.util.GeoPoint

data class GeoData(
    val id: String,
    val name: String,
    val desc: String,
    val geo: GeoPoint
)