package com.example.smartlamp.api

import com.example.smartlamp.model.BookModel
import com.example.smartlamp.model.ResponseProfileModel
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants.ACTIVE_CODE
import com.example.smartlamp.utils.Constants.BOOK_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.Urls.FAVORITES
import com.example.smartlamp.utils.Urls.LOGIN
import com.example.smartlamp.utils.Urls.PROFILE
import com.example.smartlamp.utils.Urls.REGISTER
import com.example.smartlamp.utils.Urls.VERIFY
import retrofit2.Call
import retrofit2.http.*

interface
ApiInterface {

    //Account
    @POST(LOGIN)
    fun login(
        @Body params: HashMap<String?, Any?>
    ): Call<ResponseProfileModel>

    @POST(REGISTER)
    fun register(
        @Body params: HashMap<String?, Any?>
    ): Call<SimpleApiResponse>

    @GET(VERIFY)
    fun verify(
        @Query(EMAIL) email : String,
        @Query(ACTIVE_CODE) activeCode : Int
    ): Call<SimpleApiResponse>

    @GET(PROFILE)
    fun profile(
        @Query(UID) uid : Int
    ): Call<SimpleApiResponse>

    @PUT(PROFILE)
    fun editProfile(
        @Body params: HashMap<String?, Any?>
    ): Call<SimpleApiResponse>

    //Book
    @GET(FAVORITES)
    fun getFavorites(
        @Query(UID) uid : Int,
    ): Call<BookModel>

}


