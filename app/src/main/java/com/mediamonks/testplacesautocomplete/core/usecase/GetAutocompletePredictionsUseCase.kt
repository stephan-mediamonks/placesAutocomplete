package com.mediamonks.testplacesautocomplete.core.usecase

import com.google.android.gms.maps.model.LatLng
import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import io.reactivex.Observable

/**
 * Created on 11/12/2018.
 */
interface GetAutocompletePredictionsUseCase {
    fun getAutocompletePredictions(searchTerm: String, location: LatLng?): Observable<List<PredictedPlace>>
}