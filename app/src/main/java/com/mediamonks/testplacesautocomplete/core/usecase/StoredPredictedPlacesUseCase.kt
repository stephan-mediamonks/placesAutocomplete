package com.mediamonks.testplacesautocomplete.core.usecase

import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace

/**
 * Created on 07/12/2018.
 */
interface StoredPredictedPlacesUseCase {
    fun getStoredPredictedPlaces(): List<PredictedPlace>

    fun storePredictedPlace(predictedPlace: PredictedPlace)

    fun updateStoredPredictedPlace(predictedPlace: PredictedPlace)
}