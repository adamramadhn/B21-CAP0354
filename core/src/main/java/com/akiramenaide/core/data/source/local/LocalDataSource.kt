package com.akiramenaide.core.data.source.local

import com.akiramenaide.core.data.source.local.entity.FruitEntity
import com.akiramenaide.core.data.source.local.room.FruitDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val fruitDao: FruitDao) {

    fun getAllFruits(): Flow<List<FruitEntity>> = fruitDao.getAllFruits()

    fun insertFruit(fruitEntity: FruitEntity) = fruitDao.insertFruit(fruitEntity)

    fun updateFruitInfo(fruitEntity: FruitEntity) = fruitDao.updateFruitInfo(fruitEntity)

}