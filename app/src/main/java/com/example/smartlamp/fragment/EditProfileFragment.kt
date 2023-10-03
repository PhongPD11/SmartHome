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
import com.example.smartlamp.databinding.FragmentEditProfileBinding
import com.example.smartlamp.databinding.FragmentRegisterBinding
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.CLASS_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.EXIST_EMAIL
import com.example.smartlamp.utils.Constants.EXIST_USERNAME
import com.example.smartlamp.utils.Constants.FULL_NAME
import com.example.smartlamp.utils.Constants.LAST_NAME
import com.example.smartlamp.utils.Constants.MAJOR
import com.example.smartlamp.utils.Constants.NAME
import com.example.smartlamp.utils.Constants.PASSWORD
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.Constants.USERNAME
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Validations
import com.example.smartlamp.viewmodel.AccountViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val accountViewModel: AccountViewModel by activityViewModels()
    private var isValidate = false
    private var fullName = ""
    private var firstName = ""
    private var lastName = ""
    private var classId = 0
    private var major = ""

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentEditProfileBinding.inflate(layoutInflater)

        binding.btnSend.setOnClickListener {
            sendUpdate()
        }

        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }

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

        binding.etEmail.setText(sharedPref.getString(EMAIL))
        binding.etFirstName.setText(sharedPref.getString(NAME))
        binding.etLastName.setText(sharedPref.getString(LAST_NAME))
        binding.etMajor.setText(sharedPref.getString(MAJOR))
        binding.etClassId.setText(sharedPref.getInt(CLASS_ID).toString())

        binding.etMajor.doAfterTextChanged { text ->

        }

        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pattern = Regex("^[A-Z][a-zA-Z]*( [a-zA-Z]*)?$")
                val name = s.toString().trim()
                if (name.isNotEmpty()) {
                    if (pattern.matches(name)) {
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
                if (s.isNullOrEmpty()) {
                    isValidate = true
                }
            }
        })

        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        accountViewModel.updateRes.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.code == 200) {
                    showDialogSendSuccess(requireContext())
                    sharedPref.putString(NAME, firstName)
                    sharedPref.putString(LAST_NAME, lastName)
                    sharedPref.putString(FULL_NAME, fullName)
                    sharedPref.putInt(CLASS_ID, classId)
                    sharedPref.putString(MAJOR, major)
                } else {
                    Toast.makeText(context, it.message , Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding.root
    }

    private fun TextInputEditText.onText(): String {
        return text.toString().trim()
    }

    private fun sendUpdate() {
        when {
            binding.etFirstName.onText().isEmpty() -> {
                isValidate = false
                binding.layFirstName.error = resources.getString(R.string.first_error)
            }

            else -> {
                if (isValidate) {
                    val uid = sharedPref.getInt(UID)
                    firstName = binding.etFirstName.onText()
                    lastName = binding.etLastName.onText()
                    fullName = "$firstName $lastName"
                    major = binding.etMajor.onText()
                    classId = binding.etClassId.onText().toInt()

                    saveProfile(uid,fullName, major, classId)
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

        bindingDialog.tvTitle.text = getString(R.string.update_success)

        bindingDialog.btnYes.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.navigation_profile)
        }
        dialog.show()
    }


    private fun saveProfile(
        uid: Int,
        fullName: String,
        major: String,
        classID: Int
    ) {
        accountViewModel.editProfile(
            hashMapOf(
                UID to uid,
                FULL_NAME to fullName,
                MAJOR to major,
                CLASS_ID to classID
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountViewModel.updateRes.value = null
    }

}
