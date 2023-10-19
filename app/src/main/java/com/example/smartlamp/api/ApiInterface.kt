package com.example.smartlamp.api

import com.example.smartlamp.model.*
import com.example.smartlamp.utils.Constants.ACTIVE_CODE
import com.example.smartlamp.utils.Constants.BOOK_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.FCM
import com.example.smartlamp.utils.Constants.ID
import com.example.smartlamp.utils.Constants.IS_FAVORITE
import com.example.smartlamp.utils.Constants.Star
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.Urls
import com.example.smartlamp.utils.Urls.FAVORITE
import com.example.smartlamp.utils.Urls.FAVORITES
import com.example.smartlamp.utils.Urls.GET_BOOKS
import com.example.smartlamp.utils.Urls.GET_USER_BOOK
import com.example.smartlamp.utils.Urls.LOGIN
import com.example.smartlamp.utils.Urls.NOTIFICATION
import com.example.smartlamp.utils.Urls.NOTIFICATION_DELETE
import com.example.smartlamp.utils.Urls.PROFILE
import com.example.smartlamp.utils.Urls.READ_NOTIFICATION
import com.example.smartlamp.utils.Urls.REGISTER
import com.example.smartlamp.utils.Urls.REGISTER_BOOK
import com.example.smartlamp.utils.Urls.SEND_FCM
import com.example.smartlamp.utils.Urls.USER_RATE_BOOK
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
    @GET(GET_BOOKS)
    fun getBooks(): Call<BookModel>

    @GET(GET_USER_BOOK)
    fun getUserBooks(
        @Query(UID) uid: Int
    ): Call<UserBooks>

    @POST(REGISTER_BOOK)
    fun registerBook(
        @Body params: HashMap<String?, Any?>
    ): Call<UserBook>

    @GET(USER_RATE_BOOK)
    fun userRateBook(
        @Query(UID) uid: Int,
        @Query(BOOK_ID) bookId: Long,
        @Query(Star) star: Int,
    ): Call<SimpleApiResponse>

    @GET(FAVORITES)
    fun getFavorites(
        @Query(UID) uid : Int
    ): Call<BookModel>

    @GET(FAVORITE)
    fun makeFavorite(
        @Query(UID) uid : Int,
        @Query(BOOK_ID) bookId : Long,
        @Query(IS_FAVORITE) isFavorite : Boolean
    ): Call<SimpleApiResponse>

    @GET(NOTIFICATION)
    fun getNotification(
        @Query(UID) uid : Int
    ): Call<NotificationModel>

    @POST(SEND_FCM)
    fun sendFCM(
        @Query(UID) uid : Int,
        @Query(FCM) fcm : String,
    ): Call<SimpleApiResponse>

    @POST(READ_NOTIFICATION)
    fun readNotification(
        @Query(ID) id : Int
    ): Call<SimpleApiResponse>

    @DELETE(NOTIFICATION_DELETE)
    fun deleteNotification(
        @Query(ID) id : Int
    ): Call<SimpleApiResponse>
}


