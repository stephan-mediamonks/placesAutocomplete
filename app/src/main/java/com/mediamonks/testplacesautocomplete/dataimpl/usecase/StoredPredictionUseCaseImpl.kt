package com.mediamonks.testplacesautocomplete.dataimpl.usecase

import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import com.mediamonks.testplacesautocomplete.core.repository.StoreSearchRepository
import com.mediamonks.testplacesautocomplete.core.usecase.StoredPredictedPlacesUseCase
import javax.inject.Inject

/**
 * Created on 07/12/2018.
 */
class StoredPredictionUseCaseImpl @Inject constructor(private val storeSearchRepository: StoreSearchRepository) : StoredPredictedPlacesUseCase {
    override fun getStoredPredictedPlaces(): List<PredictedPlace> {
        return storeSearchRepository.getStoredPredictedPlaces()
    }

    override fun storePredictedPlace(predictedPlace: PredictedPlace) {
        storeSearchRepository.storePredictedPlace(predictedPlace)
    }

    override fun updateStoredPredictedPlace(predictedPlace: PredictedPlace) {
        storeSearchRepository.updateStoredPredictedPlace(predictedPlace)
    }
}
