package com.example.smartlamp.database

//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.smartlamp.database.entity.Schedule

//
//@Database(entities = [Schedule::class], version = 1, exportSchema = false)
//    abstract class ScheduleDatabase : RoomDatabase() {
//
//        abstract fun getScheduleDao(): ScheduleDao
//
//        companion object {
//            @Volatile
//            private var INSTANCE: ScheduleDatabase? = null
//            fun getDatabase(context: Context): ScheduleDatabase {
//                return INSTANCE ?: synchronized(this) {
//                    val instance = Room.databaseBuilder(
//                        context.applicationContext, ScheduleDatabase::class.java,
//                        "schedule_database"
//                    )
//                        .build()
//                    INSTANCE = instance
//                    instance
//                }
//            }
//        }
//    }
