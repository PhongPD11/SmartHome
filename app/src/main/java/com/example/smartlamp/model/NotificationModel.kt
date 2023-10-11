package com.example.smartlamp.model

import org.intellij.lang.annotations.Language

data class NotificationModel(
    val code: Int,
    val data: List<Data>?,
    val message: String
) {
    data class Data(
        val id: Int,
        val uid: Int,
        val createAt: String,
        val title: String,
        val content: String,
        val isRead: Boolean,
        val type: String,
    )
}
