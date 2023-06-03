package com.cleanworld.ecoville.data

import android.util.Size
import com.cleanerworld.ecoville.R
import com.cleanworld.ecoville.model.OnBoardingItem
import com.cleanworld.ecoville.model.PlaceAutoComplete
import com.cleanworld.ecoville.model.WasteItem

object DataSource {

     val onBoardingItems = listOf(
         OnBoardingItem(1, R.drawable.plant, R.string.dispose_text, R.string.dispose_description_text),
         OnBoardingItem(2, R.drawable.ice_cream, R.string.collect_text, R.string.collect_description_text),
         OnBoardingItem(3, R.drawable.ink_layer, R.string.recycle_text, R.string.recycle_description_text)
     )
     val tabLabels= listOf("Plastic" , "E-Waste","Glass" , "Organic","Metal" )
     val tabIcons= listOf(R.drawable.ic_plastic,R.drawable.ic_ewaste,R.drawable.ic_bottle,R.drawable.ic_organic,R.drawable.ic_metal)

     val tabCount= tabLabels.size
     val wasteItems= listOf(
         WasteItem(1,R.drawable.ic_plastic_cups,"Plastic Cups","Didan Kimathi,Nyeri","200km"),
         WasteItem(2,R.drawable.ic_cartons,"Cartons and Plastic Papers","Chuka University, Chuka","15000km")
     )

    var places:ArrayList<PlaceAutoComplete> = arrayListOf()

 }

