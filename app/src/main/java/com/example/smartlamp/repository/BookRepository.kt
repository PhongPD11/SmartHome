package com.example.smartlamp.repository

import com.example.smartlamp.api.ApiInterface
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val apiInterface: ApiInterface,
) {
    fun getFavorites(uid: Int) = apiInterface.getFavorites(uid)
    fun getUserBooks(uid: Int) = apiInterface.getUserBooks(uid)
    fun getBooksByAuthorId(authorId: Int) = apiInterface.getBooks(authorId)
    fun getBooksByAuthorName(authorName: String) = apiInterface.getBooks(null, authorName)
    fun getBooksByType(type: String) = apiInterface.getBooks(null, null, type)
    fun getBooksByMajor(major: String) = apiInterface.getBooks(null, null, null, major)
    fun getTopBook() = apiInterface.getTopBooks()
    fun getBooksByLanguage(language: String) = apiInterface.getBooks(null, null, null, null, language)
    fun getBooks() = apiInterface.getBooks()
}