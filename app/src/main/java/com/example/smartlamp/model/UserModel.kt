package com.example.smartlamp.model

data class UserResponseModel(
    val code: Int,
    val data: UserEditModel,
    val message: String
)

data class UserEditModel(
    var classId: Int,
    var fullName: String = "",
    var major: String = "",
    var uid: Int,
    var imageUrl: String = ""
)
