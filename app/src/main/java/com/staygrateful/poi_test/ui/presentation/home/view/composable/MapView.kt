package com.staygrateful.poi_test.ui.presentation.home.view.composable

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.DrawableCompat
import com.staygrateful.osm.compose.MapProperties
import com.staygrateful.osm.compose.Marker
import com.staygrateful.osm.compose.OpenStreetMap
import com.staygrateful.osm.compose.ZoomButtonVisibility
import com.staygrateful.osm.compose.rememberCameraState
import com.staygrateful.osm.compose.rememberMarkerState
import com.staygrateful.osm.compose.rememberOverlayManagerState
import com.staygrateful.poi_test.Config
import com.staygrateful.poi_test.R
import com.staygrateful.poi_test.data.models.dummyCoordinatesList
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

    val coordinateList = viewModels.coordinateList.collectAsState().value

    val overlayManagerState = rememberOverlayManagerState()

    var visiblePinLocation by rememberSaveable {
        mutableStateOf(false)
    }

    val myLocationState = rememberMarkerState(
        geoPoint = viewModels.currentGeoLocation
    )

    val pinLocationState = rememberMarkerState(
        geoPoint = dummyCoordinatesList.first().geo
    )

    val cameraState = rememberCameraState {
        geoPoint = viewModels.currentGeoLocation
        zoom = 16.0
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
            cameraState.geoPoint = myLocationState.geoPoint.offsetDefault()
            cameraState.zoom = Config.MAPS_ZOOM_LEVEL
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
            onMapLongClick = {},
            onMapClick = {
                pinLocationState.geoPoint = it
                visiblePinLocation = true
            },
            onRotationListener = { value ->
                scope.launch(Dispatchers.Main) {
                    viewModels.mapOrientation.snapTo(value)
                }
            }
        ) {
            coordinateList.forEach { geoData ->
                println("Geo data : ${geoData.name}, ${geoData.id}")
                Marker(
                    state = rememberMarkerState(key = geoData.id, geoPoint = geoData.geo),
                    id = geoData.id,
                    icon = AppCompatResources.getDrawable(context, R.drawable.ic_location)?.apply {
                        DrawableCompat.setTint(this, ColorPinLocation.toArgb())
                    },
                    iconSize = 35.dp,
                    title = geoData.name,
                    snippet = geoData.address,
                    visible = true,
                    visibleInfo = true,
                    polygonClickListener = { polygon, mapView, eventPos ->
                        true
                    },
                    onClick = { marker ->
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
            }

            Marker(
                state = pinLocationState,
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_location)?.apply {
                    DrawableCompat.setTint(this, ColorPinLocation.toArgb())
                },
                iconSize = 35.dp,
                title = "Pin Location",
                snippet = "Pinned",
                visible = visiblePinLocation,
                visibleInfo = visiblePinLocation,
                polygonClickListener = { polygon, mapView, eventPos ->
                    true
                },
                onClick = { marker ->
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
                title = "My Location",
                snippet = "Current Location",
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

        MapNav()
    }
}
