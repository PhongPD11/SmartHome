package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.repository.LampRepository
import com.example.smartlamp.model.LampModel
import com.example.smartlamp.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LampViewModel @Inject constructor(private val repository: LampRepository) : ViewModel() {
    private val lampData: MutableLiveData<LampModel?> = MutableLiveData()
    private val userData: MutableLiveData<UserModel?> = MutableLiveData()
    val keyApp: MutableLiveData<String> = MutableLiveData()
    val uid: MutableLiveData<String> = MutableLiveData()

    init {
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

    fun startObservingUser(uid : String){
        userData.value = repository.observeUser(uid).value
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
    fun getKeyData(): MutableLiveData<String> {
        return keyApp
    }
    fun getUID(): MutableLiveData<String> {
        return uid
    }
    fun getUserData(): MutableLiveData<UserModel?> {
        return userData
    }
}