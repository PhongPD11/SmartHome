package com.example.smartlamp.repository

import androidx.lifecycle.MutableLiveData
import com.example.smartlamp.model.LampModel
import com.example.smartlamp.model.UserModel
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import javax.inject.Inject

class LampRepository @Inject constructor(){
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun observeLampData(listener: ValueEventListener) {
        val lampDataRef = databaseRef.child("Led")
        lampDataRef.addValueEventListener(listener)
    }

    fun observeKey(listener: ValueEventListener) {
        val lampDataRef = databaseRef.child("Key")
        lampDataRef.addValueEventListener(listener)
    }
    fun getUserData(uid: String): DatabaseReference {
        return databaseRef.child("User/$uid")
    }

}


