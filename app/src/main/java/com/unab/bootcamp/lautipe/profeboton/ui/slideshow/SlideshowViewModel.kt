package com.unab.bootcamp.lautipe.myapplication.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Banco de Preguntas"
    }
    val text: LiveData<String> = _text
}