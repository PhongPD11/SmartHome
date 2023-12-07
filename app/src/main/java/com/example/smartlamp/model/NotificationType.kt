package com.example.smartlamp.model

import android.content.res.Resources
import com.example.smartlamp.R

enum class NotificationType(val status: String) {
    CONGRATS("Chúc mừng") {
        override fun getStatusString(resources: Resources): String {
            return "Chúc mừng"
        }
        override val imageResource: Int
            get() = R.drawable.ic_congratulation
    },
    VOLUNTEER("Tình nguyện") {
        override fun getStatusString(resources: Resources): String {
            return "Tình nguyện"
        }
        override val imageResource: Int
            get() = R.drawable.ic_volunteer
    },
    ALERT("Cảnh báo") {
        override fun getStatusString(resources: Resources): String {
            return "Cảnh báo"
        }
        override val imageResource: Int
            get() = R.drawable.ic_alert
    },
    WARNING("Nhắc nhở") {
        override fun getStatusString(resources: Resources): String {
            return "Nhắc nhở"
        }
        override val imageResource: Int
            get() = R.drawable.ic_warning
    },
    NOTIFY("Thông báo") {
        override fun getStatusString(resources: Resources): String {
            return "Thông báo"
        }
        override val imageResource: Int
            get() = R.drawable.ic_bell
    };

    abstract fun getStatusString(resources: Resources): String
    abstract val imageResource: Int

}