package com.example.smartlamp.api

import com.example.smartlamp.model.*
import com.example.smartlamp.utils.Constants.ACTIVE_CODE
import com.example.smartlamp.utils.Constants.AUTHOR_ID
import com.example.smartlamp.utils.Constants.AUTHOR_NAME
import com.example.smartlamp.utils.Constants.BOOK_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.ENQUIRY_ID
import com.example.smartlamp.utils.Constants.FCM
import com.example.smartlamp.utils.Constants.ID
import com.example.smartlamp.utils.Constants.IS_FAVORITE
import com.example.smartlamp.utils.Constants.LANGUAGE
import com.example.smartlamp.utils.Constants.MAJOR
import com.example.smartlamp.utils.Constants.Star
import com.example.smartlamp.utils.Constants.TYPE
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.Urls.BORROW_BOOK
import com.example.smartlamp.utils.Urls.CHANGE_PASS
import com.example.smartlamp.utils.Urls.CREATE_ENQUIRY
import com.example.smartlamp.utils.Urls.FAVORITE
import com.example.smartlamp.utils.Urls.FAVORITES
import com.example.smartlamp.utils.Urls.GET_BOOKS
import com.example.smartlamp.utils.Urls.GET_ENQUIRY
import com.example.smartlamp.utils.Urls.GET_ENQUIRY_DETAIL
import com.example.smartlamp.utils.Urls.GET_TOP_BOOK
import com.example.smartlamp.utils.Urls.GET_USER_BOOK
import com.example.smartlamp.utils.Urls.LOGIN
import com.example.smartlamp.utils.Urls.NOTIFICATION
import com.example.smartlamp.utils.Urls.NOTIFICATION_DELETE
import com.example.smartlamp.utils.Urls.PROFILE
import com.example.smartlamp.utils.Urls.READ_NOTIFICATION
import com.example.smartlamp.utils.Urls.REGISTER
import com.example.smartlamp.utils.Urls.REGISTER_BOOK
import com.example.smartlamp.utils.Urls.RETURN_BOOK
import com.example.smartlamp.utils.Urls.SCHEDULE
import com.example.smartlamp.utils.Urls.SEND_ENQUIRY
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
        @Query(EMAIL) email: String,
        @Query(ACTIVE_CODE) activeCode: Int
    ): Call<SimpleApiResponse>

    @GET(PROFILE)
    fun getProfile(
        @Query(UID) uid: Int
    ): Call<ResponseProfileModel>

    @Multipart
    @PUT(PROFILE)
    fun editProfile(
        @Part file: MultipartBody.Part?,
        @Part("model") model: RequestBody
    ): Call<UserResponseModel>

    @PUT(CHANGE_PASS)
    fun changePassword(
        @Body params: HashMap<String?, Any?>
    ): Call<SimpleApiResponse>

    //Book
    @GET(GET_BOOKS)
    fun getBooks(
        @Query(AUTHOR_ID) authorId: Int? = null,
        @Query(AUTHOR_NAME) authorName: String? = "",
        @Query(TYPE) type: String? = "",
        @Query(MAJOR) major: String? = "",
        @Query(LANGUAGE) language: String? = "",
    ): Call<BookModel>

    @GET(GET_TOP_BOOK)
    fun getTopBooks(): Call<BookModel>

    @GET(GET_USER_BOOK)
    fun getUserBooks(
        @Query(UID) uid: Int
    ): Call<UserBooks>

    @POST(REGISTER_BOOK)
    fun registerBook(
        @Body params: HashMap<String?, Any?>
    ): Call<UserBook>

    @GET(BORROW_BOOK)
    fun borrowBook(
        @Header("Authorization") authorization: String,
        @Query("bookId") bookId: Long,
        @Query("uid") uid: Int
    ): Call<SimpleApiResponse>

    @GET(RETURN_BOOK)
    fun returnBook(
        @Header("Authorization") authorization: String,
        @Query("bookId") bookId: Long,
        @Query("uid") uid: Int
    ): Call<SimpleApiResponse>

    @GET(USER_RATE_BOOK)
    fun userRateBook(
        @Query(UID) uid: Int,
        @Query(BOOK_ID) bookId: Long,
        @Query(Star) star: Int,
    ): Call<SimpleApiResponse>

    @GET(FAVORITES)
    fun getFavorites(
        @Query(UID) uid: Int
    ): Call<BookModel>

    @GET(FAVORITE)
    fun makeFavorite(
        @Query(UID) uid: Int,
        @Query(BOOK_ID) bookId: Long,
        @Query(IS_FAVORITE) isFavorite: Boolean
    ): Call<SimpleApiResponse>

    @GET(NOTIFICATION)
    fun getNotification(
        @Query(UID) uid: Int
    ): Call<NotificationModel>

    @POST(SEND_FCM)
    fun sendFCM(
        @Query(UID) uid: Int,
        @Query(FCM) fcm: String,
    ): Call<SimpleApiResponse>

    @POST(READ_NOTIFICATION)
    fun readNotification(
        @Query(ID) id: Int
    ): Call<SimpleApiResponse>

    @DELETE(NOTIFICATION_DELETE)
    fun deleteNotification(
        @Query(ID) id: Int
    ): Call<SimpleApiResponse>

    @POST(SCHEDULE)
    fun setSchedule(
        @Body params: HashMap<String, Any>
    ): Call<SimpleApiResponse>

    @PUT(SCHEDULE)
    fun updateSchedule(
        @Body params: HashMap<String, Any>
    ): Call<SimpleApiResponse>

    @DELETE(SCHEDULE)
    fun deleteSchedule(
        @Query(ID) id: Int
    ): Call<SimpleApiResponse>

    @GET(SCHEDULE)
    fun getSchedule(
        @Query(UID) uid: Int
    ): Call<ScheduleResponse>

    @POST(SEND_ENQUIRY)
    fun sendEnquiry(
        @Body params: HashMap<String, Any>
    ): Call<SimpleApiResponse>

    @POST(CREATE_ENQUIRY)
    fun createEnquiry(
        @Body params: HashMap<String, Any>
    ): Call<SimpleApiResponse>

    @GET(GET_ENQUIRY)
    fun getEnquiry(
        @Query(UID) uid: Int
    ): Call<EnquiryResponse>

    @GET(GET_ENQUIRY_DETAIL)
    fun getEnquiryDetail(
        @Query(ENQUIRY_ID) id: Int
    ): Call<EnquiryDetailResponse>

}


