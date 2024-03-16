package com.staygrateful.osm.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Geocoder.GeocodeListener
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import org.osmdroid.util.GeoPoint
import java.util.Locale


object LocationUtils {

    const val HEADING_NORTH: Double = 0.0

    const val HEADING_EAST: Double = 90.0

    const val HEADING_SOUTH: Double = 180.0

    const val HEADING_WEST: Double = 270.0

    fun getLocationFromName(context: Context, locationName: String, onResult: (Address?) -> Unit) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocationName(locationName, 1,
                    object : GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            val address = addresses.firstOrNull()
                            if (address != null) {
                                onResult.invoke(address)
                            } else {
                                onResult.invoke(getLocationAddress(geocoder, locationName))
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            onResult.invoke(null)
                        }
                    })
            } else {
                onResult.invoke(getLocationAddress(geocoder, locationName))
            }
        } catch (e: java.lang.Exception) {
            print(e.message)
            onResult.invoke(null)
        }
    }

    fun getLocationAddress(context: Context, location: Location, onResult: (Address?) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1,
                object : GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        val address = addresses.firstOrNull()
                        if (address != null) {
                            onResult.invoke(address)
                        } else {
                            onResult.invoke(getLocationAddress(geocoder, location))
                        }
                    }

                    override fun onError(errorMessage: String?) {
                        onResult.invoke(null)
                    }
                })
        } else {
            onResult.invoke(getLocationAddress(geocoder, location))
        }
    }

    private fun getLocationAddress(geocoder: Geocoder, location: Location): Address? {
        return try {
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
                ?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    private fun getLocationAddress(geocoder: Geocoder, locationName: String): Address? {
        return try {
            geocoder.getFromLocationName(locationName, 1)
                ?.firstOrNull()
        } catch (e: Exception) {
            null
        }
    }

    fun Context.checkLocationPermission(onMissing: () -> Unit, onGranted: (() -> Unit)? = null) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onMissing()
            return
        }
        if (onGranted != null) {
            onGranted()
        }
    }

    @SuppressLint("NewApi")
    fun handlePermission(
        permissions: Map<String, @JvmSuppressWildcards Boolean>,
        onPermissionGranted: (permission: String) -> Unit,
        onPermissionDenied: () -> Unit,
    ) {
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Log.d("TAG", "requestLocationPermission: Precise location access granted")
                onPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Log.d("TAG", "requestLocationPermission: Only approximate location access granted.")
                onPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            else -> {
                Log.d("TAG", "requestLocationPermission: No location access granted.")
                onPermissionDenied()
            }
        }
    }

    fun launchPermission(locationPermissionRequest: ActivityResultLauncher<Array<String>>) {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            )
        )
    }

    fun offsetLocation(
        geoPoint: GeoPoint,
        distanceInMeter: Double,
        heading: Double
    ): GeoPoint {
        return offsetLocation(geoPoint.latitude, geoPoint.longitude, distanceInMeter, heading)
    }

    fun offsetLocation(
        latitude: Double,
        longitude: Double,
        distanceInMeter: Double,
        heading: Double
    ): GeoPoint {
        // 0 = Shift 500 meters to the north
        // 90 = Shift 500 meters to the east
        // 180 = Shift 500 meters to the south
        // 270.0 =Shift 500 meters to the west
        val latLon = SphericalUtil.computeOffset(
                LatLng(latitude, longitude), distanceInMeter, heading
            )
        return GeoPoint(latLon.latitude, latLon.longitude)
    }
}