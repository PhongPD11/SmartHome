package com.example.smartlamp.model

data class BookModel(
    val code : Int,
    val data : ArrayList<BookData>,
    val message : String
) {
    data class BookData(
        val bookId : Int,
        val name : String,
        val amount : Int,
        val author : List<String>,
        val type : List<String>,
        val vote : Double
    )
}