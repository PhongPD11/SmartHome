package com.example.smartlamp.database

import androidx.room.*
import com.example.smartlamp.database.entity.Mode


@Dao
interface ModeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(mode: Mode)

    @Delete
    fun delete(mode: Mode)

    @Update
    fun update(mode: Mode)

    @Query("SELECT * FROM modeDatabase ORDER BY id")
    fun getAllModes(): List<Mode>

}