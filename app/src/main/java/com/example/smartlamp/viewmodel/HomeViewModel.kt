package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.model.BookModel
import com.example.smartlamp.repository.BookRepository
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    sharedPref: SharedPref) : ViewModel() {

    init {
        getFavorites(sharedPref.getInt(UID))
    }

    val favorites = MutableLiveData<BookModel>()

    fun getFavorites(uid: Int) {
        bookRepository.getFavorites(uid).enqueue(object: Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                favorites.value = response.body()
            }
            override fun onFailure(call: Call<BookModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


}