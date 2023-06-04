package com.example.smartlamp.model

data class UserModel(
    var firstName: String = "",
    var lastName: String = "",
    var imageUrl: String = "",
)

data class UserSaveModel(
    var email: String,
    var password: String,
    var firstName: String = "",
    var lastName: String = "",
    var key: Int,
    var uid: String = "",
    var pin: String,
)
