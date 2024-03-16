package com.staygrateful.poi_test.ui.view.home.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.staygrateful.poi_test.data.models.NetworkResult
import com.staygrateful.poi_test.data.models.request.SearchRequest
import com.staygrateful.poi_test.data.models.response.SearchResponse
import com.staygrateful.poi_test.data.repository.Repository
import com.staygrateful.poi_test.domain.interactor.HomepageInteractor
import com.staygrateful.poi_test.domain.usecase.HomepageUseCase
import com.staygrateful.poi_test.ui.view.home.contract.HomepageContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homepageInteractor: HomepageInteractor,
    application: Application
) : AndroidViewModel(application), HomepageContract.UserActionListener {

    private val _response: MutableLiveData<NetworkResult<SearchResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<SearchResponse>> = _response

    override fun search(request: SearchRequest) {
        viewModelScope.launch {
            homepageInteractor.search(request).collect { values ->
                _response.value = values
            }
        }
    }
}