package com.mediamonks.testplacesautocomplete.core.repository

import com.google.android.gms.maps.model.LatLngBounds
import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import io.reactivex.Observable

/**
 * Created on 07/12/2018.
 */
interface StoreSearchRepository {
    fun getAutocompletePredictions(searchTerm: String, bounds: LatLngBounds?): Observable<List<PredictedPlace>>

    fun getPlaceLocation(predictedPlace: PredictedPlace): Observable<PredictedPlace>

    fun getStoredPredictedPlaces(): List<PredictedPlace>

    fun storePredictedPlace(predictedPlace: PredictedPlace)

    fun getStoredPredictedPlace(placeId: String): PredictedPlace?

    fun updateStoredPredictedPlace(predictedPlace: PredictedPlace)
}