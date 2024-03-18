package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.DrawableCompat
import com.staygrateful.osm.compose.MapProperties
import com.staygrateful.osm.compose.Marker
import com.staygrateful.osm.compose.OpenStreetMap
import com.staygrateful.osm.compose.ZoomButtonVisibility
import com.staygrateful.osm.compose.rememberCameraState
import com.staygrateful.osm.compose.rememberMarkerState
import com.staygrateful.osm.compose.rememberOverlayManagerState
import com.staygrateful.osm.extension.showToast
import com.staygrateful.poi_test.Config
import com.staygrateful.poi_test.R
import com.staygrateful.poi_test.data.models.Coordinates
import com.staygrateful.poi_test.data.models.InitialGeoPoint
import com.staygrateful.poi_test.external.extension.offsetDefault
import com.staygrateful.poi_test.ui.composables.MarkerText
import com.staygrateful.poi_test.ui.presentation.home.viewmodel.HomeViewModel
import com.staygrateful.poi_test.ui.theme.ColorPinLocation
import com.staygrateful.poi_test.ui.theme.ColorRadiusLocation
import com.staygrateful.poi_test.ui.theme.ColorSecondary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Marker

@Composable
fun MapView(
    viewModels: HomeViewModel
) {
    val scope = rememberCoroutineScope()

    val context = LocalContext.current

    val overlayManagerState = rememberOverlayManagerState()

    val myLocationState = rememberMarkerState(
        geoPoint = viewModels.currentGeoLocation
    )

    val pinLocationState = rememberMarkerState(
        geoPoint = viewModels.findAreaLocation.geo
    )

    val cameraState = rememberCameraState {
        geoPoint = viewModels.currentGeoLocation
        zoom = Config.MAPS_ZOOM_LEVEL
    }

    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isTilesScaledToDpi = true,
                tileSources = TileSourceFactory.MAPNIK,
                isEnableRotationGesture = true,
                zoomButtonVisibility = ZoomButtonVisibility.NEVER
            )
        )
    }

    LaunchedEffect(viewModels.currentGeoLocation) {
        myLocationState.geoPoint = viewModels.currentGeoLocation
        if (cameraState.geoPoint.latitude == 0.0 &&
            cameraState.geoPoint.longitude == 0.0
        ) {
            cameraState.geoPoint = myLocationState.geoPoint
            cameraState.zoom = Config.MAPS_ZOOM_LEVEL
        }
    }

    LaunchedEffect(viewModels.findAreaLocation) {
        with(viewModels.findAreaLocation) location@{
            pinLocationState.geoPoint = this.geo
            if (this.geo.latitude == 0.0 || this.geo.longitude == 0.0) return@location
            cameraState.geoPoint = this.geo
            if (this.zoomLevel != null) {
                cameraState.zoom = this.zoomLevel!!
            }
        }
    }

    LaunchedEffect(viewModels.cameraFocusLocation) {
        with(viewModels.cameraFocusLocation) location@{
            if (this.geo.latitude == 0.0 || this.geo.longitude == 0.0) return@location
            cameraState.geoPoint = this.geo
            if (this.zoomLevel != null) {
                cameraState.zoom = this.zoomLevel!!
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        OpenStreetMap(
            modifier = Modifier
                .fillMaxSize(),
            enableAutoZoom = false,
            cameraState = cameraState,
            overlayManagerState = overlayManagerState,
            properties = mapProperties,
            onMapLongClick = {
            },
            onMapClick = {
                viewModels.updateMarkerLocation(
                    Coordinates(
                        context.getString(R.string.find_this_area),
                        geo = it,
                        visible = true
                    )
                )
            },
            onRotationListener = { value ->
                scope.launch(Dispatchers.Main) {
                    viewModels.mapOrientation.snapTo(value)
                }
            }
        ) {
            Marker(
                state = pinLocationState,
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_location)?.apply {
                    DrawableCompat.setTint(this, ColorPinLocation.toArgb())
                },
                iconSize = 35.dp,
                title = viewModels.findAreaLocation.name,
                snippet = viewModels.findAreaLocation.snipet,
                visible = viewModels.findAreaLocation.visible,
                visibleInfo = viewModels.findAreaLocation.visible,
                polygonClickListener = { polygon, mapView, eventPos ->
                    true
                },
                onClick = { marker ->
                    viewModels.findAreaLocation =
                        viewModels.findAreaLocation.copy(visible = false, geo = InitialGeoPoint)
                    true
                }
            ) {
                MarkerText(
                    onClick = {

                    },
                    title = it.title,
                    contentColor = Color.Black,
                    color = Color.White,
                )
            }

            Marker(state = myLocationState,
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_gps_location),
                anchorV = Marker.ANCHOR_CENTER,
                iconSize = 29.dp,
                polygonRadius = 27.0,
                polygonColor = ColorRadiusLocation,
                title = stringResource(R.string.my_location),
                snippet = stringResource(R.string.current_location),
                visibleInfo = false,
                visible = true,
                onClick = { marker ->
                    true
                }
            ) {
                MarkerText(
                    onClick = {

                    },
                    title = it.title,
                    contentColor = Color.White,
                    color = ColorSecondary,
                )
            }
        }

        MapNav(viewModels)
    }
}
