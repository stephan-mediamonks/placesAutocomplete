package com.mediamonks.testplacesautocomplete.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mediamonks.testplacesautocomplete.R
import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace
import com.mediamonks.testplacesautocomplete.di.Injectable
import com.mediamonks.testplacesautocomplete.ui.viewmodel.PlacePredictionViewModel
import com.mediamonks.testplacesautocomplete.ui.viewmodel.PlacePredictionViewModel.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_places.*
import javax.inject.Inject

/**
 * Created on 05/12/2018.
 */
class PlacesFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val compositeDisposable = CompositeDisposable()
    private lateinit var predictedPlacesAdapter: PredictedPlacesAdapter
    private lateinit var placePredictionViewModel: PlacePredictionViewModel

    companion object {
        fun newInstance(): PlacesFragment {
            return PlacesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_places, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        compositeDisposable.add(
            placesInput.getInput().subscribe { placePredictionViewModel.getPredictions(it) }
        )

        placePredictionViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(PlacePredictionViewModel::class.java)
        placePredictionViewModel.getPredictionsResponse().observe(this, Observer { onPredictionsResponse(it) })
        placePredictionViewModel.getPlaceResponse().observe(this, Observer { onPlaceResponse(it) })
        placePredictionViewModel.getViewMode().observe(this, Observer { onViewModeChanged(it) })
        placePredictionViewModel.clearSearchTerm()

        predictedPlacesAdapter = PredictedPlacesAdapter()
        predictedPlacesAdapter.setPlaceSelectionListener { onPredictedPlaceSelected(it) }

        predictedPlacesList.layoutManager = LinearLayoutManager(context)
        predictedPlacesList.adapter = predictedPlacesAdapter
    }

    private fun onViewModeChanged(viewMode: PredictedPlacesViewMode) {
        when (viewMode) {
            is PredictedPlacesViewMode.NoSearchTerm -> setNoSearchTermView()
            is PredictedPlacesViewMode.NoPredictionsFound -> setNoPredictionsFoundView()
            is PredictedPlacesViewMode.Predictions -> setPredictionsFoundView(viewMode.predictedPlaces)
        }
    }

    private fun setPredictionsFoundView(predictedPlaces: List<PredictedPlace>) {
        predictedPlacesList.visibility = View.VISIBLE
        noSuggestionsFoundText.visibility = View.GONE

        predictedPlacesAdapter.setPredictedPlaces(predictedPlaces)
    }

    private fun setNoPredictionsFoundView() {
        predictedPlacesList.visibility = View.GONE
        noSuggestionsFoundText.visibility = View.VISIBLE
    }

    private fun setNoSearchTermView() {
        predictedPlacesList.visibility = View.GONE
        noSuggestionsFoundText.visibility = View.GONE
    }

    private fun onPredictionsResponse(loadingState: PredictionsResponse) {
        when (loadingState) {
            is PredictionsResponse.Error -> {
                hideLoadingPredictions()

                showToast(loadingState.message)
            }
            is PredictionsResponse.Loading -> showLoadingPredictions()
            else -> hideLoadingPredictions()
        }
    }

    private fun showLoadingPredictions() {
        progressSpinner.visibility = View.VISIBLE
    }

    private fun hideLoadingPredictions() {
        progressSpinner.visibility = View.GONE
    }

    private fun onPredictedPlaceSelected(predictedPlace: PredictedPlace) {
        placePredictionViewModel.storePrediction(predictedPlace)

        placePredictionViewModel.getPredictedPlaceLocation(predictedPlace)
    }

    private fun onPlaceResponse(loadingState: PlaceResponse) {
        when (loadingState) {
            is PlaceResponse.Success -> onPlaceFound(loadingState.place)
            is PlaceResponse.Error -> showToast(loadingState.message)
        }
    }

    private fun onPlaceFound(place: PredictedPlace) {
//        Timber.d { "onPlaceFound: ${place.location}" }
    }

    private fun showToast(message: String?) {
        if (message == null || context == null) return

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()

        super.onDestroyView()
    }
}
