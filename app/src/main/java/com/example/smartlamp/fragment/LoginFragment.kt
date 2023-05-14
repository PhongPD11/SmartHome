package com.example.smartlamp.fragment


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
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentLoginBinding
import com.example.smartlamp.databinding.FragmentRegisterBinding
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

        binding.etEmail.addTextChangedListener(object: TextWatcher {
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
                checkEmailRegistered(s.toString())
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
                if (isValidate){
                    val email = binding.etEmail.text.toString()
                    val pass = binding.etPassword.text.toString()
                    loginUser(email,pass)
                }
            }
        }
    }

    private fun checkEmailRegistered(email: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        binding.layUsername.error = "The email address is not registered!"
                        binding.layUsername.isErrorEnabled = true
                        isValidate = false
                    } else {
                        isValidate = true
                        binding.layUsername.error = null
                        binding.layUsername.isErrorEnabled = false
                    }
                }
            }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser: FirebaseUser? = auth.currentUser
                    var signed = false
                    if (currentUser != null) {
                        signed = true
                        lampViewModel.uid.value = currentUser.uid
                        lampViewModel.startObservingUser(currentUser.uid)
                    }
                    val bundle = bundleOf( "signed" to signed)
                    findNavController().navigate(R.id.navigation_home, bundle)
                } else {
                    binding.layPassword.isErrorEnabled = true
                    binding.layPassword.error = "Password is not true!"
                }
            }
    }


}
