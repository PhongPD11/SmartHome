package com.example.smartlamp.activity.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class LampRepository @Inject constructor(
){
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun observeLampData(listener: ValueEventListener) {
        val lampDataRef = databaseRef.child("Led")
        lampDataRef.addValueEventListener(listener)
    }
}


