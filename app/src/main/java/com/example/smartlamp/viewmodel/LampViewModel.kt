package com.example.smartlamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.smartlamp.database.entity.User
import com.example.smartlamp.repository.LampRepository
import com.example.smartlamp.model.LampModel
import com.example.smartlamp.model.UserModel
import com.example.smartlamp.model.UserSaveModel
import com.example.smartlamp.repository.UserRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LampViewModel @Inject constructor(
    private val repository: LampRepository,
) : ViewModel() {
    private val lampData: MutableLiveData<LampModel?> = MutableLiveData()
    private val userData: MutableLiveData<UserModel?> = MutableLiveData()
    val keyApp: MutableLiveData<String> = MutableLiveData()

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
//        isLogin()
        startObservingLampData()
        startObservingKey()
    }

    private fun startObservingLampData() {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val lampModel = dataSnapshot.getValue(LampModel::class.java)
                    lampData.value = lampModel
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        repository.observeLampData(listener)
    }

    fun fetchUserData(uid: String) {
        val userRef = repository.getUserData(uid)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userModel = dataSnapshot.getValue(UserModel::class.java)
                userData.value = userModel
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }

    fun getUserData(): MutableLiveData<UserModel?> {
        return userData
    }
     fun startObservingKey() {
        val listener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val key = dataSnapshot.value.toString()
                    keyApp.value = key
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        repository.observeKey(listener)
    }

    fun getLampData(): MutableLiveData<LampModel?> {
        return lampData
    }

//    fun saveUser(user: UserSaveModel) {
//        viewModelScope.launch {
//            val name = user.firstName + user.lastName
//            val email = user.email
//            val pass = user.password
//            val pin = user.pin
//            val key = user.key
//            val uid = user.uid
//
//            val newUser = User(email,pass,uid,key,pin,name)
//
//            userRepo.saveNewUser(newUser)
//            _user.value = userRepo.getUser(email)
//        }
//    }
//
//    fun getCurrentUser(email: String){
//        viewModelScope.launch {
//            _user.value = userRepo.getUser(email)
//        }
//    }

//    fun isLogin(){
//        viewModelScope.launch {
//            val check = userRepo.getListUser()
//        }
//    }

//    fun delete(user: User) {
//        viewModelScope.launch {
//            userRepo.deleteUser(user)
//        }
//    }

//    fun deleteList() {
//        viewModelScope.launch {
//            userRepo.deleteAll()
////            _check.value = userRepo.getListUser().isNotEmpty()
//        }
//    }
}