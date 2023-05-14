package com.example.smartlamp.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userDatabase")
class User(
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "uid") var uid: String,
    @ColumnInfo(name = "key") var key: Int,
    @ColumnInfo(name = "pin") var pin: String,
    @ColumnInfo(name = "name") var name: String,
){
    @PrimaryKey(autoGenerate = true)
    var id = 0
}