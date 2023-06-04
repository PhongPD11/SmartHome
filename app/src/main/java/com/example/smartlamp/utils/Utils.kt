package com.example.smartlamp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.smartlamp.R
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

        fun loadImageFromUrl(url: String, imageView: ImageView) {
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)

            Glide.with(imageView.context)
                .load(url)
                .apply(requestOptions)
                .into(imageView)
        }
    }

}