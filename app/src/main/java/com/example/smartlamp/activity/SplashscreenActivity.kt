package com.example.smartlamp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.smartlamp.R
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.model.WeatherModel
import com.example.smartlamp.utils.Constants.FCM
import com.example.smartlamp.utils.Constants.LOGIN
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {
    var weather = ArrayList<WeatherModel>()
    lateinit var sharePref : SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePref = SharedPref(this)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        FirebaseApp.initializeApp(this)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@addOnCompleteListener
                }
                val token: String = task.result.token
                if (sharePref.getBoolean(LOGIN)) {
                    val uid = sharePref.getInt(UID)
                    apiInterface.sendFCM(uid, token).enqueue(object : retrofit2.Callback<SimpleApiResponse> {
                        override fun onResponse(
                            call: Call<SimpleApiResponse>,
                            response: Response<SimpleApiResponse>
                        ) {
                            if (response.body()?.data != null) {
                                sharePref.putString(FCM, token)
                            }
                        }
                        override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })
                }
            }

        setContentView(R.layout.activity_splashscreen)
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }, 2000)
    }

}