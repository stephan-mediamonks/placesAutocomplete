package com.mediamonks.testplacesautocomplete.ui.viewmodel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.ajalt.timberkt.Timber
import com.google.android.gms.maps.model.LatLng
import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import com.mediamonks.testplacesautocomplete.core.usecase.GetAutocompletePredictionsUseCase
import com.mediamonks.testplacesautocomplete.core.usecase.GetPredictedPlaceLocationUseCase
import com.mediamonks.testplacesautocomplete.core.usecase.StoredPredictedPlacesUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created on 06/12/2018.
 */
class PlacePredictionViewModel @Inject constructor(
    private val storedPredictedPlacesUseCase: StoredPredictedPlacesUseCase,
    private val autocompletePredictionsUseCase: GetAutocompletePredictionsUseCase,
    private val predictedPlaceLocationUseCase: GetPredictedPlaceLocationUseCase
) : ViewModel() {

    sealed class PredictionsResponse {
        object Idle : PredictionsResponse()
        object Loading : PredictionsResponse()
        object Success : PredictionsResponse()
        class Error constructor(val message: String?) : PredictionsResponse()
    }

    sealed class PlaceResponse {
        object Idle : PlaceResponse()
        object Loading : PlaceResponse()
        class Error constructor(val message: String?) : PlaceResponse()
        class Success(val place: PredictedPlace) : PlaceResponse()
    }

    sealed class PredictedPlacesViewMode {
        object NoSearchTerm : PredictedPlacesViewMode()
        object NoPredictionsFound : PredictedPlacesViewMode()
        class Predictions constructor(val predictedPlaces: List<PredictedPlace>) : PredictedPlacesViewMode()
    }

    private val predictionsResponse: MutableLiveData<PredictionsResponse> = MutableLiveData()
    private var pendingRequest: String? = null
    private val placeResponse: MutableLiveData<PlaceResponse> = MutableLiveData()
    private var userLocation: LatLng? = null
    private var storedPredictions: List<PredictedPlace>? = null
    private var predictions: List<PredictedPlace>? = null
    private val viewMode: MutableLiveData<PredictedPlacesViewMode> = MutableLiveData()
    private var currentSearchTerm: String = ""
    private val compositeDisposable = CompositeDisposable()

    init {
        predictionsResponse.value = PredictionsResponse.Idle
        placeResponse.value = PlaceResponse.Idle

        storedPredictions = storedPredictedPlacesUseCase.getStoredPredictedPlaces()
    }

    fun getPredictions(searchTerm: String) {
        if (predictionsResponse.value is PredictionsResponse.Loading) {
            Timber.d { "getPredictions: still loading, so storing new request" }
            pendingRequest = searchTerm
        } else {
            loadPredictions(searchTerm)
        }
    }

    private fun loadPredictions(searchTerm: String) {
        Timber.d { "loadPredictions: $searchTerm" }

        currentSearchTerm = searchTerm

        if (currentSearchTerm.isEmpty()) {
            updateViewMode()
            return
        }

        predictionsResponse.postValue(PredictionsResponse.Loading)

        registerSubscription(
            autocompletePredictionsUseCase
                .getAutocompletePredictions(searchTerm, userLocation)
                .subscribe({
                    val newRequest = pendingRequest
                    if (newRequest != null) {
                        Timber.d { "loadPredictions: we have a pending request" }

                        loadPredictions(newRequest)

                        pendingRequest = null
                    } else {
                        Timber.d { "loadPredictions: done loading, ${it.size} suggestions found" }
                        predictions = it

                        predictionsResponse.postValue(PredictionsResponse.Success)

                        updateViewMode()
                    }
                }, {
                    Timber.e { "loadPredictions: ${it.message}" }

                    predictions = null

                    predictionsResponse.postValue(PredictionsResponse.Error(it.message))

                    updateViewMode()
                })
        )
    }

    fun getPredictedPlaceLocation(predictedPlace: PredictedPlace) {
        Timber.d { "getPredictedPlaceLocation: $predictedPlace" }

        placeResponse.postValue(PlaceResponse.Loading)

        registerSubscription(
            predictedPlaceLocationUseCase
                .getPredictedPlaceLocation(predictedPlace)
                .subscribe({
                    Timber.d { "getPredictedPlaceLocation: success! $it " }

                    // store place with location
                    it.location?.apply { storedPredictedPlacesUseCase.updateStoredPredictedPlace(it) }

                    placeResponse.postValue(PlaceResponse.Success(it))
                }, {
                    Timber.e { "getPredictedPlaceLocation: ${it.message}" }

                    placeResponse.postValue(PlaceResponse.Error(it.message))
                })
        )
    }

    fun clearSearchTerm() {
        currentSearchTerm = ""

        updateViewMode()
    }

    private fun updateViewMode() {
        if (currentSearchTerm.isEmpty()) {
            if (storedPredictions.isNullOrEmpty()) {
                viewMode.postValue(PredictedPlacesViewMode.NoSearchTerm)
            } else {
                viewMode.postValue(PredictedPlacesViewMode.Predictions(storedPredictions!!))
            }
        } else if (predictions.isNullOrEmpty()) {
            viewMode.postValue(PredictedPlacesViewMode.NoPredictionsFound)
        } else {
            viewMode.postValue(PredictedPlacesViewMode.Predictions(predictions!!))
        }
    }

    fun updateUserLocation(location: Location) {
        Timber.d { "updateUserLocation: " }

        userLocation = LatLng(location.latitude, location.longitude)
    }

    fun storePrediction(predictedPlace: PredictedPlace) {
        storedPredictedPlacesUseCase.storePredictedPlace(predictedPlace)
    }

    fun getPredictionsResponse(): LiveData<PredictionsResponse> = predictionsResponse

    fun getPlaceResponse(): LiveData<PlaceResponse> = placeResponse

    fun getViewMode(): LiveData<PredictedPlacesViewMode> = viewMode

    private fun registerSubscription(subscription: Disposable) {
        compositeDisposable.add(subscription)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.dispose()
    }
}
