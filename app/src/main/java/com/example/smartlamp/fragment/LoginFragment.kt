package com.example.smartlamp.fragment


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.activity.MainActivity
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentLoginBinding
import com.example.smartlamp.databinding.FragmentRegisterBinding
import com.example.smartlamp.model.UserSaveModel
import com.example.smartlamp.utils.Validations
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private var isValidate = false
    lateinit var auth: FirebaseAuth
    private val lampViewModel: LampViewModel by activityViewModels()


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

        auth = Firebase.auth

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val notValidate: Boolean = Validations.checkEmailValidates(
                    s.toString(),
                    binding.layUsername,
                    requireContext()
                )
                if (!notValidate) {
                    binding.layUsername.error = null
                    binding.layUsername.isErrorEnabled = false
                    isValidate = true
                } else {
                    isValidate = false
                }
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

        return binding.root
    }

    private fun TextInputEditText.onText(): String {
        return text.toString().trim()
    }

    private fun sendLogin() {
        when {
            binding.etEmail.onText().isEmpty() -> {
                isValidate = false
                binding.layUsername.error = resources.getString(R.string.email_error)
            }

            binding.etPassword.onText().isEmpty() -> {
                isValidate = false
                binding.layPassword.error = resources.getString(R.string.password_error)
            }

            else -> {
                if (isValidate) {
                    val email = binding.etEmail.text.toString()
                    val pass = binding.etPassword.text.toString()
                    loginUser(email, pass)
                }
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                } else {
                    binding.layUsername.isErrorEnabled = true
                    binding.layUsername.error = "Email or Password is not true!"
                }
            }
    }

//    private fun saveNewUser(user: FirebaseUser, email: String, password: String) {
//        val pin = "1111"
//        var firstName = ""
//        var lastName = ""
//        val key = 123456789
//        val uid = user.uid
//        lampViewModel.getUserData().observe(viewLifecycleOwner) {
//            if (it != null) {
//                firstName = it.firstName
//                lastName = it.lastName
//            }
//            val userSave = UserSaveModel(email, password, firstName, lastName, key, uid, pin)
//            lampViewModel.saveUser(userSave)
//        }
//
//    }


}
