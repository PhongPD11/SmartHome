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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.smartlamp.R
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.DialogVerifyEmailBinding
import com.example.smartlamp.databinding.FragmentRegisterBinding
import com.example.smartlamp.utils.Constants.CLASS_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.EXIST_EMAIL
import com.example.smartlamp.utils.Constants.EXIST_USERNAME
import com.example.smartlamp.utils.Constants.FULL_NAME
import com.example.smartlamp.utils.Constants.MAJOR
import com.example.smartlamp.utils.Constants.PASSWORD
import com.example.smartlamp.utils.Constants.USERNAME
import com.example.smartlamp.utils.Validations
import com.example.smartlamp.viewmodel.AccountViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    private val accountViewModel: AccountViewModel by activityViewModels()
    private var isValidate = false
    private var emailRegister = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        viewPager = requireActivity().findViewById(R.id.viewPager)
        tabLayout = requireActivity().findViewById(R.id.tabLayout)

        binding.btnSend.setOnClickListener {
            sendRegister()
        }


        binding.etMajor.doAfterTextChanged { text ->

        }



        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
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

        binding.etConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val notValidate: Boolean =
                    Validations.passwordValidate(s.toString(), binding.layConfirmPassword)
                if (!notValidate) {
                    binding.layConfirmPassword.error = null
                    binding.layConfirmPassword.isErrorEnabled = false
                    isValidate = true
                } else {
                    isValidate = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() == binding.etPassword.text.toString()) {
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

        accountViewModel.registerModel.observe(viewLifecycleOwner) {
            if (accountViewModel.isSuccess) {
                showVerifyDialog(requireContext(), emailRegister)
            } else {
                binding.progressBar.visibility = View.GONE
                when (it.message) {
                    EXIST_USERNAME -> {
                        binding.layUsername.apply {
                            error = getString(R.string.exist_username)
                            isErrorEnabled = true
                        }
                    }
                    EXIST_EMAIL -> {
                        binding.layEmail.apply {
                            error = getString(R.string.exist_email)
                            isErrorEnabled = true
                        }
                    }
                    else -> Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        accountViewModel.isVerifySuccess.observe(viewLifecycleOwner) {
            if (it) {
                showDialogSendSuccess(requireContext())
            } else {
                showVerifyDialog(requireContext(), emailRegister)
            }
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

            binding.etFirstName.onText().isEmpty() -> {
                isValidate = false
                binding.layFirstName.error = resources.getString(R.string.first_error)
            }

            binding.etMajor.onText().isEmpty() -> {
                isValidate = false
                binding.layMajor.error = "Enter your major"
            }

            binding.etClassId.onText().isEmpty() && binding.etEmail.text.toString().contains("student") -> {
                isValidate = false
                binding.layClassId.error = "Enter your class ID"
            }

            else -> {
                if (isValidate) {
                    val email = binding.etEmail.text.toString()
                    val pass = binding.etPassword.text.toString()
                    val fullName = "${binding.etFirstName.onText()} ${binding.etLastName.onText()}"
                    val major = binding.etMajor.onText()
                    val classID = binding.etClassId.onText()
                    val username = binding.etUsername.onText()

                    registerUser(email, username, pass, fullName, major, classID)
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

        bindingDialog.btnYes.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_auth)
        }
        dialog.show()
    }


    private fun registerUser(
        email: String,
        username: String,
        password: String,
        fullName: String,
        major: String,
        classID: String
    ) {
        accountViewModel.registerAccount(
            hashMapOf(
                USERNAME to username,
                PASSWORD to password,
                EMAIL to email,
                FULL_NAME to fullName,
                MAJOR to major,
                CLASS_ID to classID
            )
        )
        emailRegister = email
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

}
