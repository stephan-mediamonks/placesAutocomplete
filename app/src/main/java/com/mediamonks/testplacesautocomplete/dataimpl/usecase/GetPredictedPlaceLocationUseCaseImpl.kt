package com.mediamonks.testplacesautocomplete.dataimpl.usecase

import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import com.mediamonks.testplacesautocomplete.core.repository.StoreSearchRepository
import com.mediamonks.testplacesautocomplete.core.usecase.GetPredictedPlaceLocationUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created on 11/12/2018.
 */
class GetPredictedPlaceLocationUseCaseImpl @Inject constructor(private val storeSearchRepository: StoreSearchRepository) : GetPredictedPlaceLocationUseCase {
    override fun getPredictedPlaceLocation(predictedPlace: PredictedPlace): Observable<PredictedPlace> {
        return storeSearchRepository
            .getPlaceLocation(predictedPlace)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}