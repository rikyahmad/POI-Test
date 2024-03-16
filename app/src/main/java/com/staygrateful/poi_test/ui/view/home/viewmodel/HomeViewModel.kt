package com.staygrateful.poi_test.ui.view.home.viewmodel

import android.app.Application
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.staygrateful.osm.extension.showToast
import com.staygrateful.osm.helper.LocationBuilder
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse
import com.staygrateful.poi_test.domain.interactor.HomepageInteractor
import com.staygrateful.poi_test.ui.view.home.contract.HomepageContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homepageInteractor: HomepageInteractor,
    application: Application
) : AndroidViewModel(application), HomepageContract.UserActionListener, DefaultLifecycleObserver {

    private var locationBuilder: LocationBuilder? = null
    private val _response: MutableLiveData<NetworkResult<SearchResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<SearchResponse>> = _response

    private val minimumDistance: Float = 5f

    var currentGeoLocation by mutableStateOf(GeoPoint(0.0, 0.0))
        private set

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        locationBuilder?.registerLocationUpdate()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        locationBuilder?.unregisterLocationUpdate()
    }

    override fun setupLocationListener(activity: ComponentActivity) {
        activity.lifecycle.addObserver(this)
        locationBuilder = LocationBuilder.init(activity)
            .setMinimumDistance(minimumDistance)
            .setPermissionListener { granted ->
                activity.showToast("Granted : $granted")
            }.setErrorListener { error ->
                activity.showToast("Error : ${error.localizedMessage}")
            }
            .setLocationListener { location ->
                activity.showToast("Location update : ${location?.latitude}")
                updateLocation(location)
            }
        locationBuilder?.build()
    }

    override fun search(request: SearchRequest) {
        viewModelScope.launch {
            homepageInteractor.search(request).collect { values ->
                _response.value = values
            }
        }
    }

    private fun updateLocation(location: Location?) {
        if (location == null) return
        currentGeoLocation = GeoPoint(
            location.latitude,
            location.longitude
        )
    }
}