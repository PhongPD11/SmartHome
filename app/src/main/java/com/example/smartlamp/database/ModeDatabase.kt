package com.example.smartlamp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smartlamp.database.entity.Mode


@Database(entities = [Mode::class], version = 1, exportSchema = false)
    abstract class ModeDatabase : RoomDatabase() {

        abstract fun getModeDao(): ModeDao

        companion object {
            @Volatile
            private var INSTANCE: ModeDatabase? = null
            fun getDatabase(context: Context): ModeDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext, ModeDatabase::class.java,
                        "mode_database"
                    )
                        .build()
                    INSTANCE = instance
                    instance
                }
            }
        }
    }
