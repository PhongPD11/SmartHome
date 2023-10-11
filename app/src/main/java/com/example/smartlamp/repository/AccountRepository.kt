package com.example.smartlamp.repository

import com.example.smartlamp.api.ApiInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class AccountRepository@Inject constructor(
    private val apiInterface: ApiInterface,
){
    fun loginApp(map :HashMap<String?,Any?>) = apiInterface.login(map)
    fun register(map :HashMap<String?,Any?>) = apiInterface.register(map)
    fun verify(email: String, code : Int) = apiInterface.verify(email,code)
//    fun getProfile(uid: Long) = apiInterface.
    fun editProfile(file: MultipartBody.Part?, model: RequestBody) = apiInterface.editProfile(file, model)

}