package com.example.smartlamp.database

import androidx.room.*
import com.example.smartlamp.database.entity.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM userDatabase ORDER BY uid")
    fun getAllUsers(): List<User>

    @Query("DELETE FROM userDatabase")
    suspend fun deleteAll()

    @Query("SELECT * FROM userDatabase WHERE email = :value")
    suspend fun getUser(value: String): User
}