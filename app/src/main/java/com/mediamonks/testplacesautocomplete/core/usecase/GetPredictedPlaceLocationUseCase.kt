package com.mediamonks.testplacesautocomplete.core.usecase

import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import io.reactivex.Observable

/**
 * Created on 11/12/2018.
 */
interface GetPredictedPlaceLocationUseCase {
    fun getPredictedPlaceLocation(predictedPlace:PredictedPlace): Observable<PredictedPlace>
}