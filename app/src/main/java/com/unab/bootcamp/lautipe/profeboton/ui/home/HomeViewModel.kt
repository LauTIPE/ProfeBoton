package com.unab.bootcamp.lautipe.myapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Aquí irian las Estadisticas"
    }
    val text: LiveData<String> = _text
}