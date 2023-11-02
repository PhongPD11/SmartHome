package com.example.smartlamp.database
//
//import androidx.room.*
//import com.example.smartlamp.database.entity.Schedule


//@Dao
//interface ScheduleDao {
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insert(schedule: Schedule)
//
//    @Delete
//    fun delete(schedule: Schedule)
//
//    @Update
//    fun update(schedule: Schedule)
//
//    @Query("SELECT * FROM scheduleDatabase ORDER BY id")
//    fun getAllSchedules(): List<Schedule>
//
//}