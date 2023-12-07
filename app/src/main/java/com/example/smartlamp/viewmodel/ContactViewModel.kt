package com.example.smartlamp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.smartlamp.model.EnquiryDetailResponse
import com.example.smartlamp.model.EnquiryResponse
import com.example.smartlamp.model.NotificationModel
import com.example.smartlamp.model.ScheduleResponse
import com.example.smartlamp.repository.ContactRepository
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class ContactViewModel @Inject constructor(
    val repository: ContactRepository,
    sharedPref: SharedPref
) : ViewModel() {
    init {
        val uid = sharedPref.getInt(UID)
        getEnquiry(uid)
    }

    val enquiryList = MutableLiveData<List<EnquiryResponse.Data>?>()
    val enquiry = MutableLiveData<List<EnquiryDetailResponse.Data>?>()

    //    fun setId(id: String) {
//        repository.setId(id)
//    }
//
//    private val enquiry = repository.getEnquiry()
//

    fun getEnquiry(uid: Int){
        repository.getEnquiry(uid).enqueue(object : Callback<EnquiryResponse> {
            override fun onResponse(
                call: Call<EnquiryResponse>,
                response: Response<EnquiryResponse>
            ) {
                if (response.body() != null) {
                    enquiryList.value = response.body()!!.data
                } else {
                    enquiryList.value = null
                }
            }

            override fun onFailure(call: Call<EnquiryResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getEnquiryDetail(id: Int){
        repository.getEnquiryDetail(id).enqueue(object : Callback<EnquiryDetailResponse> {
            override fun onResponse(
                call: Call<EnquiryDetailResponse>,
                response: Response<EnquiryDetailResponse>
            ) {
                if (response.body() != null) {
                    enquiry.value = response.body()!!.data
                } else {
                    enquiry.value = null
                }
            }

            override fun onFailure(call: Call<EnquiryDetailResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
//
//    fun getEnquiryChat(): MutableLiveData<EnquiryChatModel> {
//        return repository.getEnquiryChat()
//    }
//
//    val loadTriggerEnquiry = MutableLiveData(Unit)
//    val loadTriggerEnquiryChat = MutableLiveData(Unit)
//
//    val userEnquiry = loadTriggerEnquiry.switchMap {
//        repository.getEnquiry()
//    }
//    fun loadEnquiry() {
//        loadTriggerEnquiry.value = Unit
//    }
//
//    val userEnquiryChat = loadTriggerEnquiryChat.switchMap {
//        repository.getEnquiryChat()
//    }
//    fun loadEnquiryChat(){
//        loadTriggerEnquiryChat.value = Unit
//    }
//
//    val loadTriggerCheck = MutableLiveData(Unit)
//    val check = loadTriggerCheck.switchMap {
//        repository.sendChat
//    }
//    fun updateChat(){
//        loadTriggerCheck.value = Unit
//    }
//
//    fun saveAnswer(map: HashMap<String?, Any?>) :Boolean{
//        repository.saveAnswer(map)
//        loadEnquiryChat()
//        return true
//    }
}