package com.cleanerworld.ecoville.ui.recycle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecycleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is recycle Fragment"
    }
    val text: LiveData<String> = _text
}