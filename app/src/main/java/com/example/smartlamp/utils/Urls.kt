package com.example.smartlamp.utils



object Urls {
    const val BASE_URL = "http://192.168.1.22:8080/"

    //account

    const val LOGIN = "login"
    const val REGISTER = "register"
    const val VERIFY = "verify"
    const val PROFILE = "profile"

    //book

    const val FAVORITE = "library/favorite"
    const val FAVORITES = "library/favorites"
    const val GET_BOOK = "library/book"
    const val GET_BOOKS = "library/books"
    const val GET_USER_BOOK = "library/userbook"
    const val USER_RATE_BOOK = "library/rate"

    //Notification

    const val NOTIFICATION = "notification"
    const val NOTIFICATION_DELETE = "notification/delete"
    const val READ_NOTIFICATION = "notification/read"
    const val SEND_FCM = "notification/fcm"

}


