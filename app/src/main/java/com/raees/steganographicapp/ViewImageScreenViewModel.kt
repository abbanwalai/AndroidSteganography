package com.raees.steganographicapp

import androidx.lifecycle.ViewModel

class ViewImageScreenViewModel(
    private val dao: StegImageDao
): ViewModel() {
    var stegImage: StegImage? = null

    fun loadStegImage(imageId: Int) {
        stegImage = dao.getStegImage(imageId)
    }
}