package com.example.smartlamp.repository

import com.example.smartlamp.api.ApiInterface
import javax.inject.Inject

class BookRepository@Inject constructor(
    private val apiInterface: ApiInterface,
){
    fun getFavorites(uid: Int) = apiInterface.getFavorites(uid)
    fun getUserBooks(uid: Int) = apiInterface.getUserBooks(uid)
    fun getBooks() = apiInterface.getBooks()
}