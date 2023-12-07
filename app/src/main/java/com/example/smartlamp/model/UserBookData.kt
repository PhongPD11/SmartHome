package com.example.smartlamp.model

data class UserBookData(
    val id: Int,
    val bookId: Long,
    val uid: Int,
    val createAt: String? = "",
    val createdAt: String? = "",
    val expiredAt: String? = "",
    val returnedAt: String? = "",
    val status: String? = "",
    val rate: Int? = 0,
    val isFavorite: Boolean? = false,
    val isDelivery: Boolean? = false,
    val address: String? = ""
)
