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
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentChangePasswordBinding
import com.example.smartlamp.utils.Validations
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding

    private var isValidate = false
    private val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    private var currentPassword =""
    private lateinit var credential: AuthCredential

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)

        binding.btnSend.setOnClickListener {
            sendChange()
        }


        binding.etPassword.addTextChangedListener(object: TextWatcher {
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
                    credential = EmailAuthProvider.getCredential(user?.email!!, currentPassword)
                    val pass = binding.etNewPassword.text.toString()
                    changePass(pass)
                    binding.progressBar.visibility = View.VISIBLE
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
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                showDialogSendSuccess(requireContext())
                            } else {
                                Toast.makeText(context, "Change Password Unsuccessfully", Toast.LENGTH_LONG).show()
                            }
                        }
                } else {
                    binding.layPassword.error = resources.getString(R.string.password_confirm_error)
                    binding.layPassword.isErrorEnabled = true
                }
            }
    }

}
