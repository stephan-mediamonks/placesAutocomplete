package com.mediamonks.testplacesautocomplete.dataimpl.usecase

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil
import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import com.mediamonks.testplacesautocomplete.core.repository.StoreSearchRepository
import com.mediamonks.testplacesautocomplete.core.usecase.GetAutocompletePredictionsUseCase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created on 11/12/2018.
 */
class GetAutocompletePredictionsUseCaseImpl @Inject constructor(private val storeSearchRepository: StoreSearchRepository) : GetAutocompletePredictionsUseCase {

    override fun getAutocompletePredictions(searchTerm: String, location: LatLng?): Observable<List<PredictedPlace>> {
        return storeSearchRepository
            .getAutocompletePredictions(searchTerm, computeBounds(location))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun computeBounds(location: LatLng?): LatLngBounds? {
        var bounds: LatLngBounds? = null
        location?.let {
            val northEast = SphericalUtil.computeOffset(it, 5000.0, 45.0)
            val southWest = SphericalUtil.computeOffset(it, 5000.0, 225.0)

            bounds = LatLngBounds.Builder().include(northEast).include(southWest).build()
        }
        return bounds
    }
}