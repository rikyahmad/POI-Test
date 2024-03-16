package com.staygrateful.osm.compose

import android.view.View
import com.staygrateful.osm.compose.OsmMapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class OsmInfoWindow(view: View, mapView: OsmMapView) : InfoWindow(view, mapView) {
    override fun onOpen(item: Any?) {
    }

    override fun onClose() {
    }
}