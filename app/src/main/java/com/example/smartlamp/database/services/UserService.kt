package com.example.smartlamp.database.services

import com.example.smartlamp.database.UserDao
import com.example.smartlamp.database.entity.User
import javax.inject.Inject


class UserService @Inject constructor(private val userDao: UserDao) {

//    suspend fun saveAllTokens(tokens: List<User>) {
//        userDao.insertAll(tokens)
//    }

    suspend fun saveUser(user: User) {
        userDao.insert(user)
    }
    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

}