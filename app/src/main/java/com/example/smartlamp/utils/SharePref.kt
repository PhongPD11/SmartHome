package com.example.smartlamp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.smartlamp.utils.Constants.ADDRESS
import com.example.smartlamp.utils.Constants.CLASS_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.FULL_NAME
import com.example.smartlamp.utils.Constants.IS_LOCK
import com.example.smartlamp.utils.Constants.LAST_NAME
import com.example.smartlamp.utils.Constants.LOGIN
import com.example.smartlamp.utils.Constants.MAJOR
import com.example.smartlamp.utils.Constants.NAME
import com.example.smartlamp.utils.Constants.PASSWORD
import com.example.smartlamp.utils.Constants.PHONE_NUMBER
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.Constants.USERNAME

class SharedPref(context: Context?) {
    private val sharedPreferences: SharedPreferences =
        context!!.getSharedPreferences("alo1234", Context.MODE_PRIVATE)

    init {

    }

    fun logoutApp() {
        val editor = sharedPreferences.edit()
        editor.putString(FULL_NAME, "")
        editor.putString(EMAIL, "")
        editor.putInt(CLASS_ID, 0)
        editor.putString(MAJOR, "")
        editor.putString(ADDRESS, "")
        editor.putString(PHONE_NUMBER, "")
        editor.putInt(UID, 0)
        editor.putBoolean(IS_LOCK, false)
        editor.putString(NAME, "")
        editor.putString(LAST_NAME, "")
        editor.putString(USERNAME, "")
        editor.putString(PASSWORD, "")
        editor.putBoolean(LOGIN, false)
        editor.apply()
    }

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }


    fun getString(key: String) = sharedPreferences.getString(key, "")
    fun getInt(key: String, defValue: Int = 0) = sharedPreferences.getInt(key, defValue)
    fun getBoolean(key: String) = sharedPreferences.getBoolean(key, false)
}