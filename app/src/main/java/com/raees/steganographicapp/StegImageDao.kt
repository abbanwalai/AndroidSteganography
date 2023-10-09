package com.raees.steganographicapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StegImageDao {
    @Insert
    fun insertStegImage(stegImage: StegImage)

    @Delete
    fun deleteStegImage(stegImage: StegImage)

    @Query("SELECT * FROM stegimage ORDER by id DESC")
    fun getStegImages(): List<StegImage>

    @Query("SELECT * FROM stegimage WHERE id = :imageId LIMIT 1")
    fun getStegImage(imageId: Int): StegImage
}