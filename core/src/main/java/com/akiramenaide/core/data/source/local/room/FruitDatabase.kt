package com.akiramenaide.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.akiramenaide.core.data.source.local.entity.FruitEntity

@Database(entities = [FruitEntity::class], version = 1, exportSchema = false)
abstract class FruitDatabase: RoomDatabase() {
    abstract fun fruitDao(): FruitDao
}