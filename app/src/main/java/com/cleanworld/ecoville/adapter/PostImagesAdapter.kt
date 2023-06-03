package com.cleanworld.ecoville.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cleanerworld.ecoville.R
import com.google.android.material.imageview.ShapeableImageView

class PostImagesAdapter(var postImages: MutableList<Bitmap>?): RecyclerView.Adapter<PostImagesAdapter.PostImageViewHolder>() {

    inner class PostImageViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var postImageView=itemView.findViewById<ShapeableImageView>(R.id.postimage)
        var postImageCancelButton=itemView.findViewById<ShapeableImageView>(R.id.post_image_cancel_button)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostImageViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.post_image_item,parent,false)
        return PostImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostImageViewHolder, position: Int) {
        val item= postImages?.get(position)
        holder.postImageView.setImageBitmap(item)
        holder.postImageCancelButton.setOnClickListener{
            postImages?.remove(item)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount()= postImages.let {
        if (it==null)
            0
        else
            it.size
    }
}