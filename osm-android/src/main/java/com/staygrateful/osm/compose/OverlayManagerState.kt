package com.staygrateful.osm.compose

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.OverlayManager
import org.osmdroid.views.overlay.Polygon

@SuppressLint("MutableCollectionMutableState")
class OverlayManagerState(private var _overlayManager: OverlayManager?) {

    val overlayManager: OverlayManager
        get() = _overlayManager
            ?: throw IllegalStateException("Invalid Map attached!, please add other overlay in OpenStreetMap#onFirstLoadListener")

    var mapView: MapView? = null
        private set
    
    fun setMap(mapView: MapView) {
        this.mapView = mapView
        _overlayManager = mapView.overlayManager
    }

    fun getMap(): MapView {
        return mapView ?: throw IllegalStateException("Invalid Map attached!")
    }

    private fun tryClearOverlay(index: Int) {
        try {
            val overlay = overlayManager.getOrNull(index) ?: return
            when (overlay) {
                is Marker -> {
                    overlay.closeInfoWindow()
                    overlayManager.removeAt(index)
                    tryClearOverlay(index)
                }

                is Polygon -> {
                    overlayManager.removeAt(index)
                    tryClearOverlay(index)
                }

                else -> {
                    tryClearOverlay(index + 1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearOverlay() {
        tryClearOverlay(0)
    }
}

@Composable
fun rememberOverlayManagerState() = remember {
    OverlayManagerState(null)
}