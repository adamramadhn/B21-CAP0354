package com.akiramenaide.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fruit_table")
data class FruitEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "total")
    var total: Int,

    @ColumnInfo(name = "freshTotal")
    var freshTotal: Int
)
