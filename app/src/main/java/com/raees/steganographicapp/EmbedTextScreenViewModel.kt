package com.raees.steganographicapp

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel

class EmbedTextScreenViewModel(
    private val dao: StegImageDao
): ViewModel() {
    fun addImage(
        imageBitmap: Bitmap,
        imageName: String,
        message: String,
        password: String,
        context: Context) : Boolean
    {
        if (StegImageRepository.savePhotoToInternalStorage(imageName, imageBitmap, context)) {
            val imgFile = context.filesDir.listFiles()?.filter { it?.name == "$imageName.jpg" }?.get(0)
            val stegImage = StegImage(
                name = imageName,
                imageUri = Uri.fromFile(imgFile).toString(),
                message = message,
                password = password
            )
            dao.insertStegImage(stegImage)

            return true
        }

        return false
    }
}