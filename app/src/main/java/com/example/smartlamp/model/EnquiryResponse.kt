package com.example.smartlamp.model

class EnquiryResponse(
    val code: Int,
    val data: List<Data>?,
    val message: String
) {
    data class Data(
        val id: Int,
        val uid: Int,
        val type: String,
        val title: String,
        val content: String,
        val status: String,
        val createAt: String,
    )
}
