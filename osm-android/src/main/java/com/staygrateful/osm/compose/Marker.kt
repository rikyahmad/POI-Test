package com.staygrateful.osm.compose

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.staygrateful.osm.R
import com.staygrateful.osm.extension.toPxInt
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon


data class InfoWindowData(
    val title: String, val snippet: String
)

@Composable
@OsmAndroidComposable
fun Marker(
    state: MarkerState = rememberMarkerState(),
    anchorU: Float = Marker.ANCHOR_CENTER,
    anchorV: Float = Marker.ANCHOR_BOTTOM,
    id: String? = null,
    icon: Drawable? = null,
    iconSize: Dp? = null,
    visible: Boolean = true,
    visibleInfo: Boolean = false,
    title: String? = null,
    snippet: String? = null,
    polygonRadius: Double? = null,
    polygonColor: Color = Color.Blue.copy(0.5f),
    polygonStroke: Float = 0f,
    polygonOutlineColor: Color = Color.Transparent,
    polygonClickListener: Polygon.OnClickListener? = null,
    onClick: (Marker) -> Boolean = { false },
    customInfoWindow: Boolean = false,
    infoWindowContent: @Composable ((InfoWindowData) -> Unit) = {}
) {

    val context = LocalContext.current
    val applier =
        currentComposer.applier as? MapApplier ?: throw IllegalStateException("Invalid Applier")

    ComposeNode<MarkerNode, MapApplier>(factory = {
        println("Geo Factory : $title, $id")
        val mapView = applier.mapView
        val polygon = createPolygon(
            mapView,
            state.geoPoint,
            polygonRadius,
            polygonColor,
            polygonStroke,
            polygonOutlineColor,
            polygonClickListener
        )

        val infoWindow = if (customInfoWindow) ComposeView(context).apply {
            setContent {
                infoWindowContent.invoke(InfoWindowData(title.orEmpty(), snippet.orEmpty()))
            }
        } else {
            createInfoWindow(mapView.context, title, snippet)
        }

        val marker = addMarker(
            mapView = mapView,
            anchorU = anchorU,
            anchorV = anchorV,
            initialGeoPoint = state.geoPoint,
            id = id,
            icon = icon,
            iconSize = iconSize,
            visible = visible,
            visibleInfo = visibleInfo,
            title = title,
            snippet = snippet,
            polygon = polygon,
            infoWindow = infoWindow,
            onClick = onClick
        )

        MarkerNode(
            mapView = mapView,
            markerState = state,
            marker = marker,
            polygon = polygon,
        )
    }, update = {
        update(visibleInfo) {
            if (visibleInfo) {
                marker.showInfoWindow()
            } else {
                marker.closeInfoWindow()
            }
        }
        update(state.geoPoint) {
            polygon = createPolygonRadius(
                state.geoPoint, polygon, polygonRadius ?: 0.0
            )
            marker.position = it
        }
        update(state.rotation) {
            marker.rotation = it
        }
        update(icon) {
            if (it == null) {
                marker.setDefaultIcon()
            } else {
                marker.icon = it
            }
        }
        update(visible) {
            marker.setVisible(it)
        }
        update(title) {
            val iw = marker.infoWindow.view
            if(iw is TextView) iw.text = title
        }
        applier.invalidate()
    })
}

fun createInfoWindow(context: Context, title: String?, snippet: String?): View {
    return TextView(context).apply {
        text = title
        textSize = 14f
        setBackgroundResource(R.drawable.bg_marker_info)
        setTextColor(ContextCompat.getColor(context, R.color.marker_text_color))
    }
}

fun createPolygon(
    mapView: OsmMapView,
    geoPoint: GeoPoint,
    polygonRadius: Double?,
    polygonColor: Color = Color.Blue.copy(0.5f),
    polygonStroke: Float = 0f,
    polygonOutlineColor: Color = Color.Transparent,
    polygonClickListener: Polygon.OnClickListener? = null,
): Polygon {
    return Polygon(mapView).apply {
        this.outlinePaint.color = polygonOutlineColor.toArgb()
        this.fillPaint.color = polygonColor.toArgb()
        this.fillPaint.isAntiAlias = true
        this.outlinePaint.isAntiAlias = true
        this.outlinePaint.strokeWidth = polygonStroke
        this.setOnClickListener(
            polygonClickListener
                ?: Polygon.OnClickListener { polygon, mapView, eventPos -> true }
        )
    }.let {
        if (polygonRadius != null && polygonRadius > 0) {
            createPolygonRadius(
                geoPoint, it, polygonRadius
            )
        } else {
            it
        }
    }
}

fun addMarker(
    mapView: MapView,
    anchorU: Float = Marker.ANCHOR_CENTER,
    anchorV: Float = Marker.ANCHOR_BOTTOM,
    initialGeoPoint: GeoPoint,
    id: String? = null,
    icon: Drawable? = null,
    iconSize: Dp? = null,
    visible: Boolean = true,
    visibleInfo: Boolean = false,
    title: String? = null,
    snippet: String? = null,
    infoWindow: View? = null,
    rotation: Float = 0f,
    polygon: Polygon? = null,
    onClick: (Marker) -> Boolean = { false },
): Marker {
    return Marker(mapView).apply {
        this.position = initialGeoPoint
        this.rotation = rotation

        setVisible(visible)
        icon?.let {
            val iconDrawable = if (iconSize != null && iconSize.value > 0) {
                BitmapDrawable(
                    mapView.context.resources, it.toBitmap(
                        iconSize.toPxInt().coerceAtLeast(1),
                        iconSize.toPxInt().coerceAtLeast(1)
                    )
                )
            } else {
                it
            }
            this.icon = iconDrawable
        }
        id?.let { this.id = it }
        setAnchor(anchorU, anchorV)

        if (polygon != null) {
            mapView.overlayManager.add(polygon)
        }

        mapView.overlayManager.add(this)

        this.setupListeners(onClick)

        this.infoWindow = OsmInfoWindow(
            infoWindow ?: createInfoWindow(mapView.context, title, snippet),
            mapView
        ).also { infoWindow ->
            infoWindow.view.setOnClickListener {
                if (infoWindow.isOpen) {
                    onClick.invoke(this)
                    //close()
                }
            }
        }

        if (visibleInfo) {
            this.showInfoWindow()
        } else {
            this.closeInfoWindow()
        }
    }
}

fun Marker.setupListeners(
    onMarkerClick: (Marker) -> Boolean
) {
    this.setOnMarkerClickListener { marker, map ->
        val click = onMarkerClick.invoke(marker)
        if (marker.isInfoWindowShown) {
            //marker.closeInfoWindow()
        } else {
            marker.showInfoWindow()
        }
        click
    }
}

fun createPolygonRadius(geoPoint: GeoPoint, polygon: Polygon, polygonRadius: Double): Polygon {
    val circlePoints = ArrayList<GeoPoint>()
    for (f in 0 until 360) {
        circlePoints.add(geoPoint.destinationPoint(polygonRadius, f.toDouble()));
    }
    polygon.points = circlePoints
    return polygon
}
