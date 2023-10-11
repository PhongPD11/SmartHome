package com.example.smartlamp.model

import android.content.res.Resources
import com.example.smartlamp.R

enum class NotificationType(val status: String) {
    CONGRATS("CONGRATS") {
        override fun getStatusString(resources: Resources): String {
            return "CONGRATS"
        }
        override val imageResource: Int
            get() = R.drawable.ic_congratulation
    },
    VOLUNTEER("VOLUNTEER") {
        override fun getStatusString(resources: Resources): String {
            return "VOLUNTEER"
        }
        override val imageResource: Int
            get() = R.drawable.ic_volunteer
    },
    ALERT("ALERT") {
        override fun getStatusString(resources: Resources): String {
            return "ALERT"
        }
        override val imageResource: Int
            get() = R.drawable.ic_alert
    },
    WARNING("WARNING") {
        override fun getStatusString(resources: Resources): String {
            return "WARNING"
        }
        override val imageResource: Int
            get() = R.drawable.ic_warning
    };

    abstract fun getStatusString(resources: Resources): String
    abstract val imageResource: Int

}