package com.example.smartlamp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authenticLocal")
class User(
    @ColumnInfo(name = "user") var user: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "password") var key: String,
    @ColumnInfo(name = "pin") var pin: Int,
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}