package com.example.smartlamp.fragment


import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.databinding.FragmentRegisterBinding
import com.example.smartlamp.utils.Validations
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    private var isValidate = false

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ledNodeRef: DatabaseReference = database.getReference("Led")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSend.setOnClickListener {
            sendRegister()
        }

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            val isEmailValidate: Boolean = Validations.checkEmailValidates(
                text.toString(),
                binding.layUsername,
                requireContext()
            )
            if (!isEmailValidate) {
                binding.layUsername.error = null
                binding.layUsername.isErrorEnabled = false
                isValidate = true
            } else {
                isValidate = false
            }
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            val isPasswordValidate: Boolean =
                Validations.passwordValidate(text.toString(), binding.layPassword)
            if (!isPasswordValidate) {
                binding.layUsername.error = null
                binding.layUsername.isErrorEnabled = false
                isValidate = true
            } else {
                isValidate = false
            }
        }

        binding.etConfirmPassword.doOnTextChanged { text, _, _, _ ->
            isValidate = binding.etPassword.text.toString() == text.toString()
        }

        return binding.root
    }

    private fun TextInputEditText.onText(): String {
        return text.toString().trim()
    }

    private fun sendRegister() {
        when {
            binding.etEmail.onText().isEmpty() -> {
                isValidate = false
                binding.layUsername.error = resources.getString(R.string.email_error)
            }

            binding.etPassword.onText().isEmpty() -> {
                isValidate = false
                binding.layPassword.error = resources.getString(R.string.password_error)
            }

            binding.etConfirmPassword.onText().isEmpty() -> {
                isValidate = false
                binding.layConfirmPassword.error = resources.getString(R.string.password_error)
            }

            else -> {
                if (isValidate){

                }
            }
        }
    }


}
