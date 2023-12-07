package com.example.smartlamp.model

import java.io.Serializable

data class BookModel(
    val code: Int,
    val data: ArrayList<BookData>,
    val message: String
)

data class BookData(
    val bookId: Long,
    val name: String,
    val amount: Int,
    val author: List<AuthorData>,
    val type: String,
    val rated: Double,
    val userRate: Int?,
    val imageUrl: String?,
    val major: String,
    val language: String,
    val ddc: String,
    val status: String,
    val borrowingPeriod: Int,
    val bookLocation: String
) : Serializable

data class AuthorData(
    val authorName: String,
    val authorId: Int,
)
