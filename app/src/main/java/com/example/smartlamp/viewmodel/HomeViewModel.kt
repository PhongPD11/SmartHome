package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.model.*
import com.example.smartlamp.repository.BookRepository
import com.example.smartlamp.repository.NotificationRepository
import com.example.smartlamp.utils.Constants.MAJOR
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
        getTopBook()
        val major = sharedPref.getString(MAJOR)
        if (!major.isNullOrEmpty()){
            getBookByMajor(major)
        }

        getUserBook(uid)
        getNotify(uid)
    }

    val favorites = MutableLiveData<BookModel>()
    var favoriteList = ArrayList<BookData>()
    var emptyFav = true

    val topBooks = MutableLiveData<BookModel>()
    var topBooksList = ArrayList<BookData>()

    val majorBooks = MutableLiveData<BookModel>()
    var majorBooksList = ArrayList<BookData>()
    var recommendationBookList = ArrayList<BookData>()

    val userBook = MutableLiveData<UserBooks>()
    var userBookList = ArrayList<UserBookData>()

    val authorBook = MutableLiveData<BookModel>()
    var authorBookList = ArrayList<BookData>()

    var allBooks = ArrayList<BookData>()
    val books = MutableLiveData<BookModel>()
    val notifications = MutableLiveData<List<NotificationModel.Data>?>()

    fun getFavorites(uid: Int) {
        bookRepository.getFavorites(uid).enqueue(object: Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                favorites.value = response.body()
                favoriteList.clear()
                if (response.body()?.data != null) {
                    favoriteList = response.body()?.data!!
                    emptyFav = false
                } else if (response.body()?.message == "empty"){
                    emptyFav = true
                }
            }
            override fun onFailure(call: Call<BookModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getUserBook(uid: Int) {
        bookRepository.getUserBooks(uid).enqueue(object: Callback<UserBooks> {
            override fun onResponse(call: Call<UserBooks>, response: Response<UserBooks>) {
                userBook.value = response.body()
                userBookList.clear()
                if (response.body()?.data != null) {
                    userBookList = response.body()?.data!!
                }
            }
            override fun onFailure(call: Call<UserBooks>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getBookByAuthor(authorName: String) {
        bookRepository.getBooksByAuthorName(authorName).enqueue(object: Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                authorBook.value = response.body()
                authorBookList.clear()
                if (response.body()?.data != null) {
                    authorBookList = response.body()?.data!!
                }
            }
            override fun onFailure(call: Call<BookModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getTopBook(){
        bookRepository.getTopBook().enqueue(object : Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                topBooks.value = response.body()
                topBooksList.clear()
                if (response.body()?.data != null) {
                    topBooksList = response.body()?.data!!
                }
            }

            override fun onFailure(call: Call<BookModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getBookByMajor(major: String) {
        bookRepository.getBooksByMajor(major).enqueue(object: Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                majorBooks.value = response.body()
                majorBooksList.clear()
                if (response.body()?.data != null) {
                    majorBooksList = response.body()?.data!!
                    if (recommendationBookList.isEmpty()) {
                        majorBooks.value = response.body()
                        recommendationBookList = majorBooksList
                    }
                }
            }
            override fun onFailure(call: Call<BookModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getBooks() {
        bookRepository.getBooks().enqueue(object: Callback<BookModel> {
            override fun onResponse(call: Call<BookModel>, response: Response<BookModel>) {
                books.value = response.body()
                if (response.body()?.data != null) {
                    allBooks = response.body()?.data!!
                }
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
                    } else {
                        notifications.value = null
                    }
                }
                override fun onFailure(call: Call<NotificationModel>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

}

