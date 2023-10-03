package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smartlamp.model.ResponseProfileModel
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.repository.AccountRepository
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Urls.LOGIN
import com.example.smartlamp.utils.Urls.REGISTER
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepo: AccountRepository,
    private val sharedPref: SharedPref
    ): ViewModel() {

    var loginModel = MutableLiveData<ResponseProfileModel>()
    var registerModel = MutableLiveData<SimpleApiResponse>()
    var isVerifySuccess = MutableLiveData<Boolean>()
    var isSuccess = false;

    var updateRes = MutableLiveData<SimpleApiResponse>()

    fun loginApp (map :HashMap<String?,Any?>){
        accountRepo.loginApp(map).enqueue(object: Callback<ResponseProfileModel> {
            override fun onResponse(
                call: Call<ResponseProfileModel>,
                response: Response<ResponseProfileModel>
            ) {
                if (response.body()?.code == 200 && response.body()?.data != null){
                    sharedPref.putBoolean(LOGIN, true)
                } else{
                    sharedPref.putBoolean(LOGIN, false)
                }
                loginModel.value = response.body()
                println("Message : ${response.body()?.message}")
            }

            override fun onFailure(call: Call<ResponseProfileModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun registerAccount (map :HashMap<String?,Any?>){
        accountRepo.register(map).enqueue(object: Callback<SimpleApiResponse> {
            override fun onResponse(
                call: Call<SimpleApiResponse>,
                response: Response<SimpleApiResponse>
            ) {
                isSuccess = response.body()?.code == 200 && response.body()?.data != null
                registerModel.value = response.body()
                println("Message : ${response.body()?.message}")
            }

            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun verifyMail (email: String, code : Int){
        accountRepo.verify(email, code).enqueue(object: Callback<SimpleApiResponse> {
            override fun onResponse(
                call: Call<SimpleApiResponse>,
                response: Response<SimpleApiResponse>
            ) {
                isVerifySuccess.value = response.body()?.code == 200 && response.body()?.data != null
                println("Message : ${response.body()?.message}")
            }

            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun editProfile(map :HashMap<String?,Any?>){
        accountRepo.editProfile(map).enqueue(object : Callback<SimpleApiResponse> {
            override fun onResponse(
                call: Call<SimpleApiResponse>,
                response: Response<SimpleApiResponse>
            ) {
                updateRes.value = response.body()
            }

            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}