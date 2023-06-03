package com.cleanworld.ecoville.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleanerworld.ecoville.R
import com.cleanworld.ecoville.model.OnBoardingItem

class OnBoardingItemsAdapter(private val context: Context,private val onBoardingItems:List<OnBoardingItem>):RecyclerView.Adapter<OnBoardingItemsAdapter.OnBoardingItemsViewHolder>() {
    inner class OnBoardingItemsViewHolder(view:View):RecyclerView.ViewHolder(view){
        private val onBoardingImage=view.findViewById<ImageView>(R.id.onboard_image)
        private val onBoardingTitle=view.findViewById<TextView>(R.id.onboard_title)
        private val onBoardingDescription=view.findViewById<TextView>(R.id.onboard_description)

        fun bind(context: Context, onBoardingItem: OnBoardingItem){
            onBoardingImage.setImageResource(onBoardingItem.imageRes)
            onBoardingTitle.text=context.getString(onBoardingItem.titleRes)
            onBoardingDescription.text=context.getString(onBoardingItem.descriptionRes)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingItemsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.onboard_item_container,parent,false)
        return OnBoardingItemsViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: OnBoardingItemsViewHolder, position: Int) {
        val onBoardingItem=onBoardingItems[position]
        holder.bind(context,onBoardingItem)
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }


}