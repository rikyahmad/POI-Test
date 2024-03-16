package com.staygrateful.osm.compose

import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

internal class MarkerNode(
    val mapView: OsmMapView,
    val markerState: MarkerState,
    var polygon: Polygon,
    val marker: Marker,
) : OsmAndNode {

    override fun onAttached() {
        markerState.marker = marker
    }

    override fun onRemoved() {
        markerState.marker = null
        marker.remove(mapView)
    }

    override fun onCleared() {
        markerState.marker = null
        marker.remove(mapView)
    }
}