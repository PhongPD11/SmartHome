package com.example.smartlamp.api

import com.example.smartlamp.model.*
import com.example.smartlamp.utils.Constants.ACTIVE_CODE
import com.example.smartlamp.utils.Constants.BOOK_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.ID
import com.example.smartlamp.utils.Constants.IS_FAVORITE
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.Urls
import com.example.smartlamp.utils.Urls.FAVORITE
import com.example.smartlamp.utils.Urls.FAVORITES
import com.example.smartlamp.utils.Urls.LOGIN
import com.example.smartlamp.utils.Urls.NOTIFICATION
import com.example.smartlamp.utils.Urls.NOTIFICATION_DELETE
import com.example.smartlamp.utils.Urls.PROFILE
import com.example.smartlamp.utils.Urls.REGISTER
import com.example.smartlamp.utils.Urls.VERIFY
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @PUT(PROFILE)
    fun editProfile(
        @Part file: MultipartBody.Part?,
        @Part("model") model: RequestBody
    ): Call<UserResponseModel>

    //Book
    @GET(FAVORITES)
    fun getFavorites(
        @Query(UID) uid : Int
    ): Call<BookModel>

    @GET(FAVORITE)
    fun makeFavorite(
        @Query(UID) uid : Int,
        @Query(BOOK_ID) bookId : Int,
        @Query(IS_FAVORITE) isFavorite : Boolean
    ): Call<SimpleApiResponse>

    @GET(NOTIFICATION)
    fun getNotification(
        @Query(UID) uid : Int
    ): Call<NotificationModel>

    @DELETE(NOTIFICATION_DELETE)
    fun deleteNotification(
        @Query(ID) id : Int
    ): Call<SimpleApiResponse>
}


