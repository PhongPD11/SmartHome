package com.example.smartlamp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.smartlamp.database.entity.Mode
import com.example.smartlamp.database.entity.User
import com.example.smartlamp.repository.LampRepository
import com.example.smartlamp.model.LampModel
import com.example.smartlamp.model.ModeModel
import com.example.smartlamp.model.UserModel
import com.example.smartlamp.model.UserSaveModel
import com.example.smartlamp.repository.ModeRepository
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
    private val modeRepository: ModeRepository
) : ViewModel() {
    private val lampData: MutableLiveData<LampModel?> = MutableLiveData()
    private val userData: MutableLiveData<UserModel?> = MutableLiveData()
    val image: MutableLiveData<String> = MutableLiveData()
    val keyApp: MutableLiveData<String> = MutableLiveData()

    var size = 0
    private val _modes = MutableLiveData<List<Mode>>()
    val modes: LiveData<List<Mode>>
        get() = _modes

    init {
        startObservingLampData()
        startObservingKey()
        getAllModes()
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

    fun saveMode(mode: ModeModel) {
        viewModelScope.launch {
            val name = mode.mode
            val image = mode.image
            val flicker = mode.flicker
            val brightness = mode.brightness
            val newMode = Mode(name,image,brightness,flicker)

            modeRepository.saveNewMode(newMode)
        }
    }

    fun initMode(list: List<ModeModel>) {
        viewModelScope.launch {
            var listInit = mutableListOf<Mode>()
            for (i in list.indices){
                val mode = list[i]
                val name = mode.mode
                val image = mode.image
                val flicker = mode.flicker
                val brightness = mode.brightness
                val newMode = Mode(name,image,brightness,flicker)
                listInit.add(newMode)
            }
            modeRepository.initMode(listInit)
        }
    }

    fun getAllModes(){
        viewModelScope.launch {
            _modes.value = modeRepository.getListMode()
            size = _modes.value!!.size
        }
    }

}