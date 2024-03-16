package com.staygrateful.osm.compose

import android.content.Context
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.staygrateful.osm.custom.OrientationGestureDetector
import com.staygrateful.osm.custom.OrientationGestureOverlay
import kotlinx.coroutines.awaitCancellation
import org.osmdroid.api.IGeoPoint
import org.osmdroid.events.MapListener
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

internal typealias OsmMapView = MapView

@Composable
fun rememberMapViewWithLifecycle(
    vararg mapListener: MapListener
): OsmMapView {
    val context = LocalContext.current
    val mapView = remember {
        OsmMapView(context).apply {
            /*overlayManager.add(OrientationGestureOverlay(this, object :
                OrientationGestureDetector.RotationListener {
                override fun onRotate(deltaAngle: Float) {
                    rotationListener?.invoke(this@apply, mapOrientation)
                }
            }))*/
        }
    }

    val lifecycleObserver = rememberMapLifecycleObserver(context, mapView, *mapListener)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(
    context: Context,
    mapView: OsmMapView,
    vararg mapListener: MapListener
): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    org.osmdroid.config.Configuration.getInstance()
                        .load(context, context.getSharedPreferences("osm", Context.MODE_PRIVATE))
                }

                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_DESTROY -> {
                    mapListener.onEach { mapView.removeMapListener(it) }
                }

                else -> {}
            }
        }
    }

@LayoutScopeMarker
interface OsmAndroidScope

// public enum Visibility {ALWAYS, NEVER, SHOW_AND_FADEOUT}

enum class ZoomButtonVisibility {
    ALWAYS, NEVER, SHOW_AND_FADEOUT
}

@Composable
fun OpenStreetMap(
    modifier: Modifier = Modifier,
    enableAutoZoom: Boolean = false,
    cameraState: CameraState = rememberCameraState(),
    overlayManagerState: OverlayManagerState = rememberOverlayManagerState(),
    properties: MapProperties = DefaultMapProperties,
    onInitMap: ((MapView) -> Unit)? = null,
    onRotationListener: ((Float) -> Unit)? = null,
    onMapClick: (GeoPoint) -> Unit = {},
    onMapLongClick: (GeoPoint) -> Unit = {},
    onFirstLoadListener: () -> Unit = {},
    content: (@Composable @OsmAndroidComposable OsmAndroidScope.() -> Unit)? = null
) {

    val mapView = rememberMapViewWithLifecycle()

    val mapListeners = remember {
        MapListeners()
    }.also {
        it.onMapClick = onMapClick
        it.onMapLongClick = onMapLongClick
        it.onFirstLoadListener = {
            onFirstLoadListener.invoke()
        }
    }

    val mapProperties by rememberUpdatedState(properties)
    val parentComposition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        disposingComposition {
            mapView.newComposition(parentComposition) {
                MapViewUpdater(
                    onRotationListener,
                    mapProperties,
                    mapListeners,
                    cameraState,
                    enableAutoZoom,
                    overlayManagerState
                )
                currentContent?.invoke(object : OsmAndroidScope {})
            }
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            mapView.apply {
                onInitMap?.invoke(this)
            }
        },
        update = {
            it.keepScreenOn = true
            it.controller.animateTo(
                cameraState.geoPoint,
                cameraState.zoom,
                cameraState.speed
            )
        }
    )
}

internal suspend inline fun disposingComposition(factory: () -> Composition) {
    val composition = factory()
    try {
        awaitCancellation()
    } finally {
        composition.dispose()
    }
}

private fun OsmMapView.newComposition(
    parent: CompositionContext,
    content: @Composable () -> Unit
): Composition {
    return Composition(
        MapApplier(this), parent
    ).apply {
        setContent(content)
    }
}

fun OsmMapView.centerMap(center: GeoPoint, offX: Int, offY: Int) {
    val tl: IGeoPoint = this.projection.fromPixels(0, 0)
    val br: IGeoPoint = this.projection.fromPixels(this.width, this.height)
    val newLon: Double =
        offX * (br.longitude - tl.longitude) / this.width.toDouble() + center.longitude
    val newLat: Double =
        offY * (br.latitude - tl.latitude) / this.height.toDouble() + center.latitude
    this.controller.setCenter(GeoPoint(newLat, newLon))
}