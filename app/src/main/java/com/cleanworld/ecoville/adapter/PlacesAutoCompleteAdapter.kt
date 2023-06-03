package com.cleanworld.ecoville.adapter

import android.content.Context
import android.graphics.Typeface
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleanerworld.ecoville.R
import com.cleanworld.ecoville.model.PlaceAutoComplete
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


class PlacesAutoCompleteAdapter(var mResultList:ArrayList<PlaceAutoComplete>?) : RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder>(){

    private val TAG = "PlacesAutoAdapter"



    class PredictionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val area=itemView.findViewById<TextView>(R.id.place_area)
        val address=itemView.findViewById<TextView>(R.id.place_address)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.place_recycler_item_layout,parent,false)
        return PredictionHolder(view)
    }

    override fun onBindViewHolder(holder: PredictionHolder, position: Int) {
        //val place=mResultList[position]
        holder.area.text="Kitobero"
            //place.area
        holder.address.text="Room 3A"
            //place.address

    }

    override fun getItemCount(): Int = mResultList?.size ?: 0





}