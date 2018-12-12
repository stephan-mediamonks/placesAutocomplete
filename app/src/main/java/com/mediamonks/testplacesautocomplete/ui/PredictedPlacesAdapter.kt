package com.mediamonks.testplacesautocomplete.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mediamonks.testplacesautocomplete.R
import com.mediamonks.testplacesautocomplete.core.model.PredictedPlace

/**
 * Created on 05/12/2018.
 */

typealias PlaceSelectionListener = (PredictedPlace) -> Unit

class PredictedPlacesAdapter : RecyclerView.Adapter<PredictedPlacesAdapter.PredictedPlaceViewHolder>() {

    private var placeSelectionListener: PlaceSelectionListener? = null
    private var predictedPlaces: List<PredictedPlace>? = null

    fun setPredictedPlaces(predictedPlaces: List<PredictedPlace>) {
        this.predictedPlaces = predictedPlaces

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictedPlaceViewHolder {
        return PredictedPlaceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.listitem_predictedplace, parent, false))
    }

    override fun getItemCount(): Int {
        return predictedPlaces?.size ?: 0
    }

    override fun onBindViewHolder(holder: PredictedPlaceViewHolder, position: Int) {
        val predictedPlace = predictedPlaces?.get(position) ?: return
        holder.bind(predictedPlace.primaryText, predictedPlace.secondaryText)
    }

    fun setPlaceSelectionListener(listener: PlaceSelectionListener) {
        placeSelectionListener = listener
    }

    private fun onPlaceSelected(position: Int) {
        predictedPlaces?.get(position)?.let { placeSelectionListener?.invoke(it) }
    }

    inner class PredictedPlaceViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val primaryTextView:TextView = itemView.findViewById(R.id.primaryText)
        private val secondaryTextView:TextView = itemView.findViewById(R.id.secondaryText)

        fun bind(primaryText: String?, secondaryText:String?) {
            primaryTextView.text = primaryText ?: ""
            secondaryTextView.text = secondaryText ?: ""

            itemView.setOnClickListener { onPlaceSelected(adapterPosition) }
        }
    }
}
