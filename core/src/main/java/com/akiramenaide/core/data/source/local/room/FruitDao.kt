package com.akiramenaide.core.data.source.local.room

import androidx.room.*
import com.akiramenaide.core.data.source.local.entity.FruitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FruitDao {
    @Query("SELECT * FROM fruit_table")
    fun getAllFruits(): Flow<List<FruitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFruit(fruitEntity: FruitEntity)

    @Update
    fun updateFruitInfo(fruitEntity: FruitEntity)
}