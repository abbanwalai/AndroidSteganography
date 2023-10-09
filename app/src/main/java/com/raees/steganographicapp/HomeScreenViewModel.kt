package com.raees.steganographicapp

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class HomeScreenViewModel(
    private val dao: StegImageDao
): ViewModel() {

    var photos by mutableStateOf<List<StegImage>>(listOf())
        private set

    fun getPhotos(context: Context) {
        viewModelScope.launch {
            photos = dao.getStegImages()
        }


    }


}