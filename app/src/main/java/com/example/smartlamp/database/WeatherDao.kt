//package com.example.smartlamp.database
//
//import androidx.lifecycle.LiveData
//import androidx.room.*
//
//@Dao
//interface WeatherDao {
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(weather: Weather)
//
//    @Delete
//    fun delete(weather: Weather)
//
//    @Update
//    fun update(weather: Weather)
//
//    @Query("SELECT * FROM tableWeather ORDER BY id")
//    fun getAllWeathers(): LiveData<List<Weather>>
//}