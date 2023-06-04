package com.example.smartlamp.fragment


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
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
import com.example.smartlamp.databinding.FragmentRegisterBinding
import com.example.smartlamp.utils.Validations
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout


    private val viewModel: LampViewModel by activityViewModels()

    private var isValidate = false
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var keyApp = ""
    private val userNodeRef: DatabaseReference = database.getReference("User")

    lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        auth = Firebase.auth

        viewPager = requireActivity().findViewById(R.id.viewPager)
        tabLayout = requireActivity().findViewById(R.id.tabLayout)

        binding.btnSend.setOnClickListener {
            sendRegister()
        }

        keyApp = viewModel.keyApp.value!!

        binding.etFirstName.doAfterTextChanged { text ->
            val notValidate: Boolean =
                Validations.nameValidate(text.toString().trim(), binding.layFirstName)
            if (!notValidate) {
                binding.layFirstName.error = null
                binding.layFirstName.isErrorEnabled = false
                isValidate = true
            } else {
                isValidate = false
            }
        }

        binding.etKey.doAfterTextChanged { text ->
            if (text.toString() == keyApp) {
                binding.layKey.error = null
                binding.layKey.isErrorEnabled = false
                isValidate = true
            } else {
                binding.layKey.error = "Your key is not true"
                binding.layKey.isErrorEnabled = true
                isValidate = false
            }
        }

        binding.etLastName.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pattern = Regex("^[A-Z][a-zA-Z ]*$")
                val name = s.toString().trim()
                if (name.isNotEmpty()) {
                    if (pattern.matches(name)){
                        binding.layLastName.error = null
                        binding.layLastName.isErrorEnabled = false
                        isValidate = true
                    } else {
                        isValidate = false
                        binding.layLastName.error = "Your name is Invalid"
                        binding.layLastName.isErrorEnabled = true
                    }

                } else {
                    isValidate = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()){
                    isValidate = true
                }
            }
        })

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
                if (s.toString() != "" && s.toString() != " ") {
                        checkEmailRegistered(s.toString())
                    }
                }
        })

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
                if (s.toString() == binding.etPassword.text.toString()){
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

            else -> {
                if (isValidate){
                    val email = binding.etEmail.text.toString()
                    val pass = binding.etPassword.text.toString()
                    val firstName = binding.etFirstName.onText()
                    val lastName = binding.etLastName.onText()
                    registerUser(email,pass,firstName,lastName)
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun checkEmailRegistered(email: String) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (!signInMethods.isNullOrEmpty()) {
                        binding.layUsername.error = "This email is already in use!"
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
            findNavController().navigate(R.id.navigation_auth)
        }
        dialog.show()
    }

    private fun createName(firstName: String, lastName: String, uid : String){
        userNodeRef.child("User").setValue(uid)
        Handler().postDelayed({
            userNodeRef.child("$uid/firstName").setValue(firstName)
            userNodeRef.child("$uid/lastName").setValue(lastName)
            userNodeRef.child("$uid/imageUrl").setValue("")
        }, 2000)
    }

    private fun registerUser(email: String, password: String,firstName: String, lastName: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                task ->
            if (task.isSuccessful) {
                val currentUser: FirebaseUser? = auth.currentUser
                if (currentUser != null) {
                    createName(firstName, lastName, currentUser.uid)
                }
                showDialogSendSuccess(requireContext())
            } else {
                Toast.makeText(context, "Register Unsuccessfully", Toast.LENGTH_LONG).show()
            }
        }
    }

}
