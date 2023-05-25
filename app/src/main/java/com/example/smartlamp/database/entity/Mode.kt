package com.example.smartlamp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "modeDatabase")
class Mode(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "image") var image: Int,
    @ColumnInfo(name = "brightness") var brightness: Float,
    @ColumnInfo(name = "flickering") var flickering: Int,
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}