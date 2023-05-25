package com.example.smartlamp.repository

import com.example.smartlamp.database.entity.Mode
import com.example.smartlamp.database.entity.User
import com.example.smartlamp.database.services.ModeService
import com.example.smartlamp.database.services.UserService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModeRepository @Inject constructor(
    private val service: ModeService,
    private val dispatcher: CoroutineDispatcher,
) {

    private var modes: MutableList<Mode> = mutableListOf()
//    private var user: User = User("","","",-1,"","")

    suspend fun getListMode(): List<Mode> = withContext(dispatcher) {
        val savedMode = service.getAllModes()
        modes = savedMode.toMutableList()
        modes
    }

//    suspend fun getUser(email: String): User = withContext(dispatcher) {
//        val savedUser = service.getUser(email)
//        user = savedUser
//        user
//    }

    suspend fun saveNewMode(add: Mode) =
        withContext(dispatcher) {
            service.saveMode(add)
        }

    suspend fun initMode(list: List<Mode>) =
        withContext(dispatcher) {
            list.forEach(){
                service.saveMode(it)
            }
        }

    suspend fun deleteUser(mode: Mode) = withContext(dispatcher) {
        service.deleteMode(mode)
    }


}