package com.example.smartlamp.utils

import android.widget.ImageView
import com.example.smartlamp.R

class Utils {
    companion object {
        private val dotw = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

        fun repeatDisplay(list: ArrayList<Int>): String {
            var everyDay = true
            var timeDisplay = ""
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

        fun updateTime(hour: Int, min: Int): String {
            var display = ""
            var displayHour = ""
            var displayMin = ""
            displayHour = if (hour < 10) {
                "0${hour}"
            } else {
                hour.toString()
            }
            displayMin = if (min < 10) {
                "0${min}"
            } else {
                min.toString()
            }
            display = "$displayHour:$displayMin"
            return display
        }

        fun showRating(rate: Double, vararg stars: ImageView) {
            val starDrawables = listOf(
                R.drawable.ic_star,
                R.drawable.ic_star_half,
                R.drawable.ic_star_empty
            )
            val filledStars = rate.toInt()
            var haveHalfStar = (rate - filledStars >= 0.5)

            for (i in stars.indices) {
                if (i+1 <= filledStars) {
                    stars[i].setImageResource(starDrawables[0])
                    println("Star: $i Full star")
                } else {
                    if (haveHalfStar) {
                        stars[i].setImageResource(starDrawables[1])
                        println("Star: $i Half star")
                    }
                    break
                }
            }
        }
    }
}