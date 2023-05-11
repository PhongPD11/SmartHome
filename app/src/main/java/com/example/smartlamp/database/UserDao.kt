package com.example.smartlamp.database

import androidx.room.*
import com.example.smartlamp.database.entity.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

//    @Insert
//    fun insertAll(list: List<User>)

    @Query("DELETE FROM authenticLocal")
    suspend fun deleteAll()

    @Query("SELECT * FROM authenticLocal")
    fun getAllUsers(): List<User>
}