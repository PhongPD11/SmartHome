package com.example.smartlamp.utils

import com.google.firebase.auth.FirebaseAuth

class Utils {
    companion object{
        private val dotw = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        fun repeatDisplay(list: ArrayList<Int>) : String{
            var everyDay = true
            var timeDisplay =""
            if (list[7] == 1) {
                timeDisplay = "Once"
                everyDay = false
            } else {
                for (i in 0 until list.size - 1) {
                    if (list[i] == 1) {
                        timeDisplay += "${dotw[i]} "
                    } else {
                        everyDay = false
                    }
                }
            }
            if (everyDay && list[7] == 0) {
                timeDisplay = "Daily"
            }
            return timeDisplay
        }

        fun updateTime(hour: Int, min: Int) : String{
            var display = ""
            var displayHour = ""
            var displayMin = ""
            displayHour = if (hour < 10 ){
                "0${hour}"
            } else {
                hour.toString()
            }
            displayMin = if (min<10){
                "0${min}"
            } else {
                min.toString()
            }
            display = "$displayHour:$displayMin"
            return display
        }

        fun checkAuthentication(): Boolean {
            val auth: FirebaseAuth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            return user != null
        }
    }
}