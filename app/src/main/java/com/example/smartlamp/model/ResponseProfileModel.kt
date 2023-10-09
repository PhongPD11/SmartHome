package com.example.smartlamp.model

data class ResponseProfileModel (
    val code: Int,
    val data: Data,
    val message: String
) {
    data class Data(
        val uid: Int,
        val fullName: String,
        val email: String,
        val classId: Int,
        val major: String,
        val token: String,
        val imageUrl: String?=""
    )
}