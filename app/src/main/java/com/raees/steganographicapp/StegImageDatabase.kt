package com.raees.steganographicapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StegImage::class],
    version = 1
)
abstract class StegImageDatabase: RoomDatabase() {
    abstract val dao: StegImageDao
}