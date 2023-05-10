package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.activity.repository.LampRepository
import com.example.smartlamp.model.LampModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LampViewModel @Inject constructor(private val repository: LampRepository) : ViewModel() {
    private val lampData: MutableLiveData<LampModel?> = MutableLiveData()

    init {
        startObservingLampData()
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

    fun getLampData(): MutableLiveData<LampModel?> {
        return lampData
    }
}