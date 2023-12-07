package com.example.smartlamp.fragment


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentChangePasswordBinding
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants.NEW_PASSWORD
import com.example.smartlamp.utils.Constants.PASSWORD
import com.example.smartlamp.utils.Constants.USERNAME
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Validations
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordFragment : Fragment(), OnItemSingleClickListener {
    private lateinit var binding: FragmentChangePasswordBinding

    private var isValidate = false
    private var currentPassword =""

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        val singleClick = SingleClickListener(this)
        sharedPref = SharedPref(context)

        binding.btnSend.setOnClickListener(singleClick)
        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.etPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etNewPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val notValidate: Boolean =
                    Validations.passwordValidate(s.toString(), binding.layPassword)
                if (!notValidate) {
                    binding.layPassword.error = null
                    binding.layPassword.isErrorEnabled = false
                    isValidate = true
                } else {
                    isValidate = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etConfirmPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val notValidate: Boolean =
                    Validations.passwordValidate(s.toString() ,binding.layConfirmPassword)
                if (!notValidate) {
                    binding.layConfirmPassword.error = null
                    binding.layConfirmPassword.isErrorEnabled = false
                    isValidate = true
                } else {
                    isValidate = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == binding.etNewPassword.text.toString()){
                    isValidate = true
                    binding.layConfirmPassword.error = null
                    binding.layConfirmPassword.isErrorEnabled = false
                } else {
                    isValidate = false
                    binding.layConfirmPassword.error = getString(R.string.password_confirm_error)
                    binding.layConfirmPassword.isErrorEnabled = true
                }
            }
        })

        return binding.root
    }

    private fun TextInputEditText.onText(): String {
        return text.toString().trim()
    }

    private fun sendChange() {
        when {
            binding.etPassword.onText().isEmpty() -> {
                isValidate = false
                binding.layPassword.error = resources.getString(R.string.password_error)
            }

            binding.etNewPassword.onText().isEmpty() -> {
                isValidate = false
                binding.layPassword.error = resources.getString(R.string.password_error)
            }

            binding.etConfirmPassword.onText().isEmpty() -> {
                isValidate = false
                binding.layConfirmPassword.error = resources.getString(R.string.password_error)
            }

            else -> {
                if (isValidate){
                    currentPassword = binding.etPassword.text.toString()
                    if (currentPassword != sharedPref.getString(PASSWORD)) {
                        binding.layPassword.error = resources.getString(R.string.password_wrong)
                    } else {
                        binding.layPassword.error = null
                        binding.layPassword.isErrorEnabled = false
                        val pass = binding.etNewPassword.text.toString()
                        changePass(pass)
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showDialogSendSuccess(context: Context) {
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogSuccessBinding = DialogSuccessBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        binding.progressBar.visibility = View.GONE
        val window = dialog.window
        val params = window?.attributes

        params?.gravity = Gravity.CENTER
        window?.attributes = params

        bindingDialog.tvTitle.text = getString(R.string.register_success)

        bindingDialog.btnYes.setOnClickListener{
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_profile)
        }
        dialog.show()
    }


    private fun changePass(newPassword: String) {
        apiInterface.changePassword(hashMapOf(
            USERNAME to sharedPref.getString(USERNAME),
            PASSWORD to sharedPref.getString(PASSWORD),
            NEW_PASSWORD to newPassword
        )).enqueue(object : Callback<SimpleApiResponse>{
            override fun onResponse(
                call: Call<SimpleApiResponse>,
                response: Response<SimpleApiResponse>
            ) {
                if (response.body()?.code == 200) {
                    showDialogSendSuccess(context!!)
                    sharedPref.putString(PASSWORD, newPassword)
                } else {
                    Toast.makeText(context, "Fail to change password", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.btn_send -> sendChange()
        }
    }

}
