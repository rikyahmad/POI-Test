package com.staygrateful.osm.helper

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.staygrateful.osm.extension.showToast
import com.staygrateful.osm.utils.LocationUtils
import com.staygrateful.osm.utils.LocationUtils.checkLocationPermission

class LocationBuilder private constructor(private val activity: ComponentActivity) {

    private var minimumDistance: Float = 1f //in meter
    private var minInterval: Long = 3_000L

    private var onLocationUpdate: ((Location?) -> Unit)? = null
    private var onPermissionResult: ((Boolean) -> Unit)? = null
    private var onError: ((Throwable) -> Unit)? = null

    private val locationManager by lazy {
        activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    }

    private val locationPermissionRequest = activity.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        LocationUtils.handlePermission(
            permissions,
            {
                requestLocationUpdate()
                onPermissionResult?.invoke(true)
            },
            {
                onPermissionResult?.invoke(false)
            }
        )
    }

    fun setMinimumDistance(minimumDistance: Float) = apply {
        this.minimumDistance = minimumDistance
    }

    fun setMinimumInterval(minimumInterval: Long) = apply {
        this.minInterval = minimumInterval
    }

    fun setPermissionListener(onPermissionResult: (Boolean) -> Unit) = apply {
        this.onPermissionResult = onPermissionResult
    }

    fun setLocationListener(onLocationUpdate: (Location?) -> Unit) = apply {
        this.onLocationUpdate = onLocationUpdate
    }

    fun setErrorListener(onError: (Throwable) -> Unit) = apply {
        this.onError = onError
    }

    fun requestLocationPermission() {
        LocationUtils.launchPermission(locationPermissionRequest)
    }

    private fun lastLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val location: Location? =
            locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            //onLocationUpdate?.invoke(location)
        } else {
            onError?.invoke(Throwable("Location unknown!"))
        }
    }

    private fun requestLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        lastLocationUpdate()
        registerLocationUpdate()
    }

    fun registerLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        locationManager?.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            minInterval,
            minimumDistance,
            locationListener
        )

        locationManager?.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            minInterval,
            minimumDistance,
            locationListener
        )
    }

    fun unregisterLocationUpdate() {
        try {
            locationManager?.removeUpdates(
                locationListener
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            println("LOCATION UPDATED!")
            onLocationUpdate?.invoke(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            //activity.showToast("Status changed : $provider, $status, $extras")
        }

        override fun onProviderDisabled(provider: String) {
            //super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            //super.onProviderEnabled(provider)
        }
    }

    fun build() {
        try {
            activity.checkLocationPermission(
                onMissing = {
                    requestLocationPermission()
                },
                onGranted = {
                    requestLocationUpdate()
                    onPermissionResult?.invoke(true)
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            onError?.invoke(e)
        }
    }

    companion object {

        fun init(activity: ComponentActivity): LocationBuilder {
            return LocationBuilder(activity)
        }
    }
}