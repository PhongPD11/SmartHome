package com.example.smartlamp.model

data class UserBook(
    val code: Int,
    val data: ArrayList<UserBookData>,
    val message: String
)

data class UserBookData(
    val id: Int,
    val bookId: Int,
    val uid: Int,
    val createAt: String? = "",
    val createdAt: String? = "",
    val expireAt: String? = "",
    val returnedAt: String? = "",
    val status: String? = "",
    val rate: Int? = 0,
    val isFavorite: Boolean? = false,
    val isDelivery: Boolean? = false,
    val address: String? = ""
)
