package com.example.smartlamp.model

class EnquiryDetailResponse(
    val code: Int,
    val data: List<Data>,
    val message: String
) {
    data class Data(
        val id: Int,
        val uid: Int,
        val enquiryId: Int,
        val content: String,
        val time: String,
        val isAdmin: Boolean
    )
}
