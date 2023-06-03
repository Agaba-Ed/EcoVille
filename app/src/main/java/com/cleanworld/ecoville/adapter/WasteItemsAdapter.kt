package com.cleanworld.ecoville.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cleanerworld.ecoville.R
import com.cleanworld.ecoville.data.DataSource.wasteItems
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView


class WasteItemsAdapter(val wasteImages: MutableList<Bitmap>?) :RecyclerView.Adapter<WasteItemsAdapter.WasteItemViewHolder>() {
    class WasteItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var wasteItemImage=itemView.findViewById<ShapeableImageView>(R.id.waste_item_image)
        var wasteItemName=itemView.findViewById<MaterialTextView>(R.id.waste_item_name)
        var wasteItemLocation= itemView.findViewById<MaterialTextView>(R.id.waste_item_location)
        var wasteItemDistance=itemView.findViewById<MaterialTextView>(R.id.waste_item_distance)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteItemViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.waste_item,null,false)
        return WasteItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: WasteItemViewHolder, position: Int) {
        val wasteItem= wasteItems[position]
        val wasteImage= wasteImages!![position]

        holder.wasteItemImage.setImageBitmap(wasteImage)
        holder.wasteItemName.text=wasteItem.name
        holder.wasteItemLocation.text=wasteItem.location
        holder.wasteItemDistance.text=wasteItem.distance
    }

    override fun getItemCount(): Int = wasteImages?.size ?: 0
}