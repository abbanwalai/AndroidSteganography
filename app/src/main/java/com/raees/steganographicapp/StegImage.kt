package com.raees.steganographicapp

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StegImage (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val imageUri: String,
    val message: String,
    val password: String
)