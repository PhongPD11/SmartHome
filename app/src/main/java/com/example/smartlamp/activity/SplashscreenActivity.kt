package com.example.smartlamp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.smartlamp.R
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.model.ResponseProfileModel
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.model.WeatherModel
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.FCM
import com.example.smartlamp.utils.Constants.LOGIN
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {
    var weather = ArrayList<WeatherModel>()
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPref(this)
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
                if (sharedPref.getBoolean(LOGIN)) {
                    val uid = sharedPref.getInt(UID)

                    apiInterface.getProfile(uid).enqueue(object : Callback<ResponseProfileModel> {
                        override fun onResponse(
                            call: Call<ResponseProfileModel>,
                            response: Response<ResponseProfileModel>
                        ) {
                            if (response.body()?.code == 200 && response.body()?.data != null) {
                                val data = response.body()!!.data
                                val firstName =
                                    data.fullName.substring(0, data.fullName.indexOf(" "))
                                val lastName =
                                    data.fullName.substring(firstName.length.plus(1) ?: 0)
                                sharedPref.putString(Constants.NAME, firstName)
                                sharedPref.putString(Constants.LAST_NAME, lastName)
                                sharedPref.putString(Constants.FULL_NAME, data.fullName)
                                sharedPref.putString(Constants.EMAIL, data.email)
                                sharedPref.putInt(Constants.CLASS_ID, data.classId)
                                sharedPref.putString(Constants.MAJOR, data.major)
                                sharedPref.putInt(UID, data.uid)
                                if (!data.imageUrl.isNullOrEmpty()) {
                                    sharedPref.putString(Constants.IMAGE_URL, data.imageUrl)
                                }
                                sharedPref.putBoolean(Constants.IS_LOCK, data.status == "Khóa")
                            }
                        }

                        override fun onFailure(call: Call<ResponseProfileModel>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })

                    apiInterface.sendFCM(uid, token).enqueue(object : Callback<SimpleApiResponse> {
                        override fun onResponse(
                            call: Call<SimpleApiResponse>,
                            response: Response<SimpleApiResponse>
                        ) {
                            if (response.body()?.data != null) {
                                sharedPref.putString(FCM, token)
                            }
                        }

                        override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                            t.printStackTrace()
                        }
                    })

                }
            }

        setContentView(R.layout.activity_splashscreen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )

            } else {
                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }, 2000)
            }
        }
    }

    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Handler().postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, 2000)
            } else {

            }
        }
    }

}