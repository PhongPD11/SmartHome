package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.model.*
import com.example.smartlamp.repository.BookRepository
import com.example.smartlamp.repository.NotificationRepository
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
    private val notifyRepo: NotificationRepository,
    sharedPref: SharedPref) : ViewModel() {

    init {
        val uid = sharedPref.getInt(UID)
        getFavorites(uid)
        getBooks()
        getUserBook(uid)
        getNotify(uid)
    }

    val favorites = MutableLiveData<BookModel>()
    var favoriteList = ArrayList<BookData>()

    val userBook = MutableLiveData<UserBook>()
    var userBookList = ArrayList<UserBookData>()

    val books = MutableLiveData<BookModel>()
    val notifications = MutableLiveData<List<NotificationModel.Data>?>()

    fun getFavorites(uid: Int) {
        bookRepository.getFavorites(uid).enqueue(object: Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                favorites.value = response.body()
                favoriteList.clear()
                if (response.body()?.data != null) {
                    favoriteList = response.body()?.data!!
                }
            }
            override fun onFailure(call: Call<BookModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getUserBook(uid: Int) {
        bookRepository.getUserBook(uid).enqueue(object: Callback<UserBook> {
            override fun onResponse(call: Call<UserBook>, response: Response<UserBook>) {
                userBook.value = response.body()
                favoriteList.clear()
                if (response.body()?.data != null) {
                    userBookList = response.body()?.data!!
                }
            }
            override fun onFailure(call: Call<UserBook>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getBooks() {
        bookRepository.getBooks().enqueue(object: Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                books.value = response.body()
            }
            override fun onFailure(call: Call<BookModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getNotify(uid: Int){
            notifyRepo.getNotify(uid).enqueue(object : Callback<NotificationModel> {
                override fun onResponse(
                    call: Call<NotificationModel>,
                    response: Response<NotificationModel>
                ) {
                    if (response.body()?.data != null) {
                        notifications.value = response.body()!!.data
                    }
                }

                override fun onFailure(call: Call<NotificationModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }


}