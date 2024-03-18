package com.staygrateful.poi_test.ui.presentation.home.viewmodel

import android.app.Application
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.staygrateful.osm.extension.showToast
import com.staygrateful.osm.helper.LocationBuilder
import com.staygrateful.poi_test.Config
import com.staygrateful.poi_test.R
import com.staygrateful.poi_test.data.models.Coordinates
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.BusinessRequest
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.AutocompleteResponse
import com.staygrateful.poi_test.data.models.response.BusinessDetailsResponse
import com.staygrateful.poi_test.data.models.response.BusinessPhotoResponse
import com.staygrateful.poi_test.data.models.response.BusinessReviewResponse
import com.staygrateful.poi_test.data.models.response.SearchResponse
import com.staygrateful.poi_test.domain.interactor.HomepageInteractor
import com.staygrateful.poi_test.external.util.HandlerUtil
import com.staygrateful.poi_test.ui.navigation.Screen
import com.staygrateful.poi_test.ui.presentation.home.contract.HomepageContract
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

    private val _businessReviewsResponse: MutableLiveData<List<BusinessReviewResponse.Data>> =
        MutableLiveData()

    val businessReviewsResponse: LiveData<List<BusinessReviewResponse.Data>> =
        _businessReviewsResponse

    private val _businessPhotosResponse: MutableLiveData<List<BusinessPhotoResponse.Data>> =
        MutableLiveData()

    val businessPhotosResponse: LiveData<List<BusinessPhotoResponse.Data>> =
        _businessPhotosResponse

    private val _businessDetailsResponse: MutableLiveData<List<BusinessDetailsResponse.Data>> =
        MutableLiveData()

    val businessDetailsResponse: LiveData<List<BusinessDetailsResponse.Data>> =
        _businessDetailsResponse

    private val _searchResponse: MutableLiveData<NetworkResult<SearchResponse>> = MutableLiveData()

    val searchResponse: LiveData<NetworkResult<SearchResponse>> = _searchResponse

    private val _autocompletedResponse: MutableLiveData<List<AutocompleteResponse.Data>?> =
        MutableLiveData()

    val autocompletedResponse: LiveData<List<AutocompleteResponse.Data>?> =
        _autocompletedResponse

    var currentGeoLocation by mutableStateOf(GeoPoint(0.0, 0.0))
        private set

    var locationDetailSelected: SearchResponse.Data? = null
        private set

    private val minimumDistance: Float = 5f

    var mapOrientation by mutableStateOf(Animatable(0f))

    var searchLoading by mutableStateOf(false)

    var visibleSearchCancel by mutableStateOf(false)

    var findAreaLocation by mutableStateOf(Coordinates.Initial)

    var cameraFocusLocation by mutableStateOf(Coordinates.Initial)

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
                if(!granted) {
                    activity.showToast(activity.getString(R.string.required_location_permission_access))
                }
            }.setErrorListener { error ->
                activity.showToast(
                    activity.getString(
                        R.string.get_location_failed_with_message,
                        error.localizedMessage
                    ))
            }
            .setLocationListener { location ->
                updateLocation(location)
            }
        locationBuilder?.build()
    }

    override fun search(query: String) {
        searchLoading = true
        _searchResponse.value = NetworkResult.Success(SearchResponse.Initial)
        val request = if (findAreaLocation.visible) {
            SearchRequest.inArea(
                query = query,
                zoom = Config.MAPS_ZOOM_LEVEL.toInt(),
                lat = findAreaLocation.geo.latitude,
                lng = findAreaLocation.geo.longitude
            )
        } else {
            SearchRequest.nearBy(
                query = query,
                lat = currentGeoLocation.latitude,
                lng = currentGeoLocation.longitude
            )
        }
        viewModelScope.launch {
            if (findAreaLocation.visible) {
                homepageInteractor.searchInArea(request).collect { values ->
                    _searchResponse.value = values
                    searchLoading = false
                }
            } else {
                homepageInteractor.searchNearby(request).collect { values ->
                    _searchResponse.value = values
                    searchLoading = false
                }
            }
        }
    }

    override fun autocompleted(request: SearchRequest) {
        HandlerUtil.collectAtLeast(300, request) {
            if (it.query.isEmpty()) {
                _autocompletedResponse.value = listOf()
                return@collectAtLeast
            }
            viewModelScope.launch {
                homepageInteractor.autocompleted(it).collect { values ->
                    _autocompletedResponse.value = values.data?.data
                }
            }
        }
    }

    override fun businessDetails(request: BusinessRequest) {
        viewModelScope.launch {
            homepageInteractor.businessDetails(request).collect { values ->
                _businessDetailsResponse.value = values.data?.data ?: listOf()
            }
        }
    }

    override fun businessPhotos(request: BusinessRequest) {
        viewModelScope.launch {
            homepageInteractor.businessPhotos(request).collect { values ->
                _businessPhotosResponse.value = values.data?.data ?: listOf()
            }
        }
    }

    override fun businessReviews(request: BusinessRequest) {
        viewModelScope.launch {
            homepageInteractor.businessReviews(request).collect { values ->
                _businessReviewsResponse.value = values.data?.data ?: listOf()
            }
        }
    }

    override fun navigateToLocationDetails(
        navController: NavHostController,
        data: SearchResponse.Data
    ) {
        // Clear last selected data
        locationDetailSelected = data
        _businessDetailsResponse.value = listOf()
        _businessPhotosResponse.value = listOf()
        _businessReviewsResponse.value = listOf()
        if (data.business_id != null) {
            businessDetails(BusinessRequest.details(data.business_id, extractEmailsContacts = true))
            businessPhotos(BusinessRequest.photos(data.business_id, limit = 10))
            businessReviews(BusinessRequest.reviews(data.business_id, limit = 5))
        }
        navController.navigate(Screen.LocationDetailScreen.route)
    }

    override fun updateMarkerLocation(coordinates: Coordinates) {
        findAreaLocation = coordinates
    }

    override fun focusMapsCamera(coordinates: Coordinates) {
        cameraFocusLocation = coordinates
    }

    private fun updateLocation(location: Location?) {
        if (location == null) return
        currentGeoLocation = GeoPoint(
            location.latitude,
            location.longitude
        )
    }
}