package com.example.smartlamp.database.services

import com.example.smartlamp.database.ModeDao
import com.example.smartlamp.database.UserDao
import com.example.smartlamp.database.entity.Mode
import com.example.smartlamp.database.entity.User
import javax.inject.Inject


class ModeService @Inject constructor(private val modeDao: ModeDao) {

//    suspend fun saveAllTokens(tokens: List<User>) {
//        userDao.insertAll(tokens)
//    }

    suspend fun saveMode(mode: Mode) {
        modeDao.insert(mode)
    }
    suspend fun deleteMode(mode: Mode) {
        modeDao.delete(mode)
    }

    suspend fun updateMode(mode: Mode){
        modeDao.update(mode)
    }

    suspend fun getAllModes(): List<Mode> {
        return modeDao.getAllModes()
    }

}