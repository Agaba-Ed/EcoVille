package com.cleanerworld.ecoville.ui.add

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.imageview.ShapeableImageView

class AddViewModel : ViewModel() {
    fun addImage(imageBitmap: Bitmap) {
        _postImages.value?.add(imageBitmap)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is add Fragment"
    }
    val text: LiveData<String> = _text

    private val _postImages= MutableLiveData<MutableList<Bitmap>>().apply {
        value = mutableListOf()
    }
    val  postImages:LiveData<MutableList<Bitmap>> = _postImages

}