package com.mediamonks.testplacesautocomplete.core.model

import com.google.android.gms.maps.model.LatLng

/**
 * Created on 07/12/2018.
 */
data class PredictedPlace(
    val placeId: String,
    val primaryText: String? = null,
    val secondaryText: String? = null,
    val location: LatLng? = null
)