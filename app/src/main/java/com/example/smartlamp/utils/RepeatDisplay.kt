package com.example.smartlamp.utils

class RepeatDisplay {
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
    }
}