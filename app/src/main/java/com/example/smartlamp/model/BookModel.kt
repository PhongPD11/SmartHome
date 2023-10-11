package com.example.smartlamp.model

import java.io.Serializable

data class BookModel(
    val code: Int,
    val data: ArrayList<BookData>,
    val message: String
)
    data class BookData(
        val bookId: Int,
        val name: String,
        val amount: Int,
        val author: List<String>,
        val type: String,
        val rated: Double,
        val userRate: Int?,
        val imageUrl: String?,
        val major: String,
        val language: String
    ): Serializable
//    {
//        override fun toString(): String {
//            return "BookData(bookId=$bookId, " +
//                    "name='$name', " +
//                    "amount=$amount, " +
//                    "author='$author', " +
//                    "type='$type', " +
//                    "rated=$rated, " +
//                    " userRate=$userRate, " +
//                    "imageUrl='$imageUrl', " +
//                    "major=$major, " +
//                    "language=$language)"
//        }
//    }
