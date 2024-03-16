package com.staygrateful.osm.compose

import com.staygrateful.osm.compose.OsmAndNode
import com.staygrateful.osm.compose.OsmMapView
import org.osmdroid.views.overlay.Polyline

internal class PolylineNode(
    private val mapView: OsmMapView,
    val polyline: Polyline,
    var onPolylineClick: (Polyline) -> Unit
) : OsmAndNode {

    override fun onRemoved() {
        super.onRemoved()
        mapView.overlayManager.remove(polyline)
    }

    fun setupListeners() {
        polyline.setOnClickListener { polyline, _, _ ->
            onPolylineClick.invoke(polyline)
            if (polyline.isInfoWindowOpen) {
                polyline.closeInfoWindow()
            } else {
                polyline.showInfoWindow()
            }
            true
        }
    }
}