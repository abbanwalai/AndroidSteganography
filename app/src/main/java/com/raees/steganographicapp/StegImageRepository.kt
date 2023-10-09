package com.raees.steganographicapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object StegImageRepository {
    fun savePhotoToInternalStorage(filename: String, bmp: Bitmap?, context: Context): Boolean {
        return try {
            context.openFileOutput("$filename.jpg", Context.MODE_PRIVATE).use { stream ->
                if(!bmp?.compress(Bitmap.CompressFormat.JPEG, 100, stream)!!) {
                    throw IOException("Couldn't save bitmap.")
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun loadPhotosFromInternalStorage(context: Context): List<InternalStoragePhoto> {
        return withContext(Dispatchers.IO){
            val files = context.filesDir.listFiles()

            files.filter { it.canRead() && it.isFile && it.name.endsWith(".jpg") }.map {
                InternalStoragePhoto(it.name,  Uri.fromFile(it))
            }
        }

    }

    fun deletePhotoFromInternalStorage(filename: String, context: Context): Boolean {
        return try {
            context.deleteFile(filename)
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}