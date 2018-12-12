package com.mediamonks.testplacesautocomplete.dataimpl.repository

import android.content.Context
import com.google.android.gms.location.places.AutocompleteFilter
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.model.LatLngBounds
import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import com.mediamonks.testplacesautocomplete.core.repository.StoreSearchRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Created on 07/12/2018.
 */
class StoreSearchRepositoryImpl @Inject constructor(context: Context) : StoreSearchRepository {

    private val storedPredictedPlaces: MutableList<PredictedPlace> = ArrayList()
    private val geoDataClient: GeoDataClient = Places.getGeoDataClient(context)
    private val autocompleteFilter = AutocompleteFilter.Builder().build()
    private val autocompleteSubject: PublishSubject<List<PredictedPlace>> = PublishSubject.create()
    private val placeSubject: PublishSubject<PredictedPlace> = PublishSubject.create()

    override fun getAutocompletePredictions(searchTerm: String, bounds: LatLngBounds?): Observable<List<PredictedPlace>> {
        geoDataClient
            .getAutocompletePredictions(searchTerm, bounds, autocompleteFilter)
            .addOnSuccessListener {
                val predictions: MutableList<PredictedPlace> = ArrayList()
                it.forEach {
                    if (it.placeId != null) {
                        predictions.add(PredictedPlace(it.placeId!!, it.getPrimaryText(null).toString(), it.getSecondaryText(null).toString()))
                    }
                }

                autocompleteSubject.onNext(predictions)
            }
            .addOnFailureListener {
                autocompleteSubject.onError(it)
            }

        return autocompleteSubject
    }

    override fun getPlaceLocation(predictedPlace: PredictedPlace): Observable<PredictedPlace> {
        geoDataClient
            .getPlaceById(predictedPlace.placeId)
            .addOnSuccessListener {
                placeSubject.onNext(predictedPlace.copy(location = it.get(0).latLng))
            }
            .addOnFailureListener {
                placeSubject.onError(it)
            }

        return placeSubject
    }

    override fun getStoredPredictedPlaces(): List<PredictedPlace> = storedPredictedPlaces

    override fun storePredictedPlace(predictedPlace: PredictedPlace) {
        if (getStoredPredictedPlace(predictedPlace.placeId) != null) return

        storedPredictedPlaces += predictedPlace
    }

    override fun getStoredPredictedPlace(placeId: String): PredictedPlace? {
        return storedPredictedPlaces.firstOrNull { it.placeId == placeId }
    }

    override fun updateStoredPredictedPlace(predictedPlace: PredictedPlace) {
        storedPredictedPlaces.remove(getStoredPredictedPlace(predictedPlace.placeId))

        storePredictedPlace(predictedPlace)
    }
}