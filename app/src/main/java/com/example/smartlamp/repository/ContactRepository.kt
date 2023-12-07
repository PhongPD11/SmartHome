package com.example.smartlamp.repository

import androidx.lifecycle.MutableLiveData
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.utils.SharedPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class ContactRepository @Inject constructor(
    val apiInterface: ApiInterface
) {
    fun getEnquiry(uid: Int) = apiInterface.getEnquiry(uid)
    fun getEnquiryDetail(id: Int) = apiInterface.getEnquiryDetail(id)
    fun sendEnquiry(map: HashMap<String, Any>) = apiInterface.sendEnquiry(map)
}

//    var userId: String? = ""
//
//    fun setId(id: String) {
//        userId = id
//    }
//
//
//    fun getEnquiry(): MutableLiveData<EnquiryModel> {
//        apiInterface.getEnquiry(walletId!!)
//            .enqueue(object : Callback<EnquiryModel> {
//                override fun onResponse(
//                    call: Call<EnquiryModel>,
//                    response: Response<EnquiryModel>
//                ) {
//                    if (response.body() != null) {
//                        val data = response.body()
//                        enquiryList.postValue(data!!)
//                    }
//                }
//
//                override fun onFailure(call: Call<EnquiryModel>, t: Throwable) {
//                    t.printStackTrace()
//                }
//            })
//        return enquiryList
//    }
//
//    fun getEnquiryChat(): MutableLiveData<EnquiryChatModel> {
//        apiInterface.getEnquiryChat(userId!!)
//            .enqueue(object : Callback<EnquiryChatModel> {
//                override fun onResponse(
//                    call: Call<EnquiryChatModel>,
//                    response: Response<EnquiryChatModel>
//                ) {
//                    if (response.body() != null) {
//                        val data = response.body()
//                        chatList.postValue(data!!)
//                    }
//                }
//
//                override fun onFailure(call: Call<EnquiryChatModel>, t: Throwable) {
//                    t.printStackTrace()
//                }
//            })
//        return chatList
//    }
//
//
//    fun saveAnswer(answer: HashMap<String?, Any?>): Boolean {
//        apiInterface.saveAnswer(answer)
//            .enqueue(object : Callback<SaveAnswerModel> {
//                override fun onResponse(
//                    call: Call<SaveAnswerModel>,
//                    response: Response<SaveAnswerModel>
//                ) {
//                    sendChat.value = response.body()
//                    check = true
//                }
//
//                override fun onFailure(call: Call<SaveAnswerModel>, t: Throwable) {
//                    t.printStackTrace()
//                    check = false
//                }
//            })
//        return check
//    }
//
//    fun markReadEnquiry() {
//        apiInterface.markReadEnquiry(userId!!)
//            .enqueue(object : Callback<EnquiryModel> {
//                override fun onResponse(
//                    call: Call<EnquiryModel>,
//                    response: Response<EnquiryModel>
//                ) {
//                }
//
//                override fun onFailure(
//                    call: Call<EnquiryModel>,
//                    t: Throwable
//                ) {
//                    t.printStackTrace()
//                }
//            })
//    }
//}
//
