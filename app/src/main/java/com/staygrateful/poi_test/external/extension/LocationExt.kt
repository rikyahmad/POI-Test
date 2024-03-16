package com.staygrateful.poi_test.external.extension

import com.staygrateful.osm.utils.LocationUtils
import org.osmdroid.util.GeoPoint

fun GeoPoint.offsetDefault(
    distanceInMeter: Double = 0.0 //200
): GeoPoint {
    return LocationUtils.offsetLocation(this, distanceInMeter, LocationUtils.HEADING_SOUTH)
}