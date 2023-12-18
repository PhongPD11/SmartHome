package com.example.smartlamp.utils



object Urls {
    const val BASE_URL = "http://192.168.1.6:8080/"
//    const val BASE_URL = "http://192.168.2.57:8080/"

    //Account

    const val LOGIN = "login"
    const val REGISTER = "register"
    const val VERIFY = "verify"
    const val PROFILE = "profile"
    const val CHANGE_PASS = "password"

    //Book

    const val FAVORITE = "library/favorite"
    const val FAVORITES = "library/favorites"
    const val GET_TOP_BOOK = "library/books/top"
    const val GET_BOOKS = "library/books"

    const val GET_USER_BOOK = "library/userbook"
    const val USER_RATE_BOOK = "library/rate"
    const val REGISTER_BOOK = "library/borrow/register"
    const val BORROW_BOOK = "library/borrow"
    const val RETURN_BOOK = "library/borrow/return"


    //Notification

    const val NOTIFICATION = "notification"
    const val NOTIFICATION_DELETE = "notification/delete"
    const val READ_NOTIFICATION = "notification/read"
    const val SEND_FCM = "notification/fcm"

    //Schedule
    const val SCHEDULE = "schedule"

    //Contact
    const val SEND_ENQUIRY = "contact/send"
    const val CREATE_ENQUIRY = "contact/create"
    const val GET_ENQUIRY = "contact/user"
    const val GET_ENQUIRY_DETAIL = "contact/detail"

}


