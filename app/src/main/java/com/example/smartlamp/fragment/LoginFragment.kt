package com.example.smartlamp.fragment


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.smartlamp.R
import com.example.smartlamp.activity.MainActivity
import com.example.smartlamp.databinding.DialogVerifyEmailBinding
import com.example.smartlamp.databinding.FragmentLoginBinding
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.CLASS_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.FCM
import com.example.smartlamp.utils.Constants.FULL_NAME
import com.example.smartlamp.utils.Constants.IMAGE_URL
import com.example.smartlamp.utils.Constants.IS_LOCK
import com.example.smartlamp.utils.Constants.LAST_NAME
import com.example.smartlamp.utils.Constants.LOGIN
import com.example.smartlamp.utils.Constants.MAJOR
import com.example.smartlamp.utils.Constants.NAME
import com.example.smartlamp.utils.Constants.PASSWORD
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.Constants.USERNAME
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Validations
import com.example.smartlamp.viewmodel.AccountViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private var isValidate = false

    @Inject
    lateinit var sharedPref: SharedPref
    private val accountViewModel: AccountViewModel by activityViewModels()

    var email = ""
    private var username = ""
    private var pass = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.btnSend.setOnClickListener {
            sendLogin()
        }

        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etPassword.doAfterTextChanged { text ->
            val notValidate: Boolean =
                Validations.passwordValidate(text.toString(), binding.layPassword)
            if (!notValidate) {
                binding.layPassword.error = null
                binding.layPassword.isErrorEnabled = false
                isValidate = true
            } else {
                isValidate = false
            }
        }

        accountViewModel.loginModel.observe(viewLifecycleOwner) {
            if (sharedPref.getBoolean(LOGIN)) {
                val data = it.data
                val  firstName = data.fullName.substring(data.fullName.lastIndexOf(" ") + 1)
                val  lastName = data.fullName.substring(0, data.fullName.lastIndexOf(" "))
                sharedPref.putString(NAME, firstName)
                sharedPref.putString(LAST_NAME, lastName)
                sharedPref.putString(FULL_NAME, data.fullName)
                sharedPref.putString(EMAIL, data.email)
                sharedPref.putInt(CLASS_ID, data.classId)
                sharedPref.putString(MAJOR, data.major)
                sharedPref.putInt(UID, data.uid)
                if (!data.imageUrl.isNullOrEmpty()){
                    sharedPref.putString(IMAGE_URL, data.imageUrl)
                }
                sharedPref.putBoolean(IS_LOCK, data.status == "Khóa")
                sharedPref.putString(USERNAME, username)
                sharedPref.putString(PASSWORD, pass)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                when {
                    Constants.USER_NOT_FOUND == it.message -> {
                        binding.layUsername.apply {
                            error = getString(R.string.user_not_found)
                            isErrorEnabled = true
                        }
                    }
                    Constants.INVALID_PASSWORD == it.message -> {
                        binding.layPassword.apply {
                            error = getString(R.string.invalid_password)
                            isErrorEnabled = true
                        }
                    }
                    it.message.contains("notVerify") -> {
                        val emailMatch = Regex("email: (\\S+)").find(it.message)
                        email = emailMatch?.groupValues?.get(1).toString()
                        if (!email.isNullOrEmpty()){
                            showVerifyDialog(requireContext(), email)
                        } else Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    }
                    else -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        accountViewModel.isVerifySuccess.observe(viewLifecycleOwner) {
            if (it) {
                username = binding.etUsername.text.toString()
                pass = binding.etPassword.text.toString()
                loginUser(username, pass)
            } else {
                showVerifyDialog(requireContext(), email)
            }
        }

        return binding.root
    }

    private fun showVerifyDialog(context: Context, email: String) {
        val dialog = Dialog(context, R.style.CustomDialogTheme)
        val bindingDialog: DialogVerifyEmailBinding =
            DialogVerifyEmailBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes
        bindingDialog.btnYes.isEnabled = false
        var code = 0

        bindingDialog.etCode.doAfterTextChanged { text ->
            if (text != null) {
                if (text.length != 6) {
                    bindingDialog.btnYes.isEnabled = false
                } else {
                    bindingDialog.btnYes.isEnabled = true
                    code = text.toString().toInt()
                }
            }
        }

        bindingDialog.btnYes.setOnClickListener {
            accountViewModel.verifyMail(email, code)
            dialog.dismiss()
        }

        params?.gravity = Gravity.CENTER
        window?.attributes = params

        dialog.show()
    }

    private fun TextInputEditText.onText(): String {
        return text.toString().trim()
    }

    private fun sendLogin() {
        when {
            binding.etUsername.onText().isEmpty() -> {
                isValidate = false
                binding.layUsername.error = resources.getString(R.string.email_error)
            }

            binding.etPassword.onText().isEmpty() -> {
                isValidate = false
                binding.layPassword.error = resources.getString(R.string.password_error)
            }

            else -> {
                if (isValidate) {
                    username = binding.etUsername.text.toString()
                    pass = binding.etPassword.text.toString()
                    loginUser(username, pass)
                }
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        accountViewModel.loginApp(
            hashMapOf(
                USERNAME to username,
                PASSWORD to password,
                FCM to sharedPref.getString(FCM)
            )
        )
    }

}
