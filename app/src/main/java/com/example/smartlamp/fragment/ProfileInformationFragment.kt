package com.example.smartlamp.fragment


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
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
import com.example.smartlamp.R
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentProfileInformationBinding
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.utils.Validations
import com.example.smartlamp.viewmodel.LampViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class ProfileInformationFragment : Fragment() {
    private lateinit var binding: FragmentProfileInformationBinding

    private val viewModel: LampViewModel by activityViewModels()

    private var firstName = ""
    private var lastName = ""
    private var imageUrl = ""

    private var isValidate = false
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val userNodeRef: DatabaseReference = database.getReference("User")
    private val storageReference = FirebaseStorage.getInstance().reference
    private val currentUser = auth.currentUser
    val uid = currentUser?.uid

    var bitmap: Bitmap? = null
    private val pickImage = 100


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileInformationBinding.inflate(layoutInflater)

        binding.btnSend.setOnClickListener {
            sendRegister()
        }

        imageUrl = viewModel.image.value ?: ""

        viewModel.getUserData().observe(viewLifecycleOwner) {
            if (it != null) {
                firstName = it.firstName
                lastName = it.lastName
                imageUrl = it.imageUrl
            }

            if (binding.etFirstName.text.toString().isNullOrEmpty()) {
                binding.etFirstName.setText(firstName)
            }
            if (binding.etLastName.text.toString().isNullOrEmpty()) {
                binding.etLastName.setText(lastName)
            }
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


        binding.etLastName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val pattern = Regex("^[A-Z][a-zA-Z ]*$")
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

        binding.ivCamera.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (imageUrl.isNotEmpty()){
            Utils.loadImageFromUrl(imageUrl, binding.ivAvatar)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            val imageUri = data?.data

            val options = UCrop.Options()
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
            val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "myImage.png"))
            val intent = UCrop.of(imageUri!!, destinationUri)
                .withAspectRatio(1f, 1f)
                .withOptions(options)
                .getIntent(requireContext())
            startActivityForResult(intent, UCrop.REQUEST_CROP)

        } else if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri: Uri? = UCrop.getOutput(data!!)
            val inputStream = requireContext().contentResolver?.openInputStream(resultUri!!)!!
            bitmap = BitmapFactory.decodeStream(inputStream)
            binding.ivAvatar.setImageBitmap(bitmap)
        }
    }


    private fun TextInputEditText.onText(): String {
        return text.toString().trim()
    }

    private fun sendRegister() {
        when {

            binding.etFirstName.onText().isEmpty() -> {
                isValidate = false
                binding.layFirstName.error = resources.getString(R.string.first_error)
            }

            else -> {
                if (isValidate) {
                    val firstName = binding.etFirstName.onText()
                    val lastName = binding.etLastName.onText()
                    onSave(firstName, lastName, currentUser?.uid!!)
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

        bindingDialog.btnYes.setOnClickListener{
            dialog.dismiss()
            Handler().postDelayed({
                findNavController().navigate(R.id.navigation_profile)
            }, 2000)
        }
        dialog.show()
    }

    private fun onSave(firstName: String, lastName: String, uid: String) {
        if (bitmap != null) {
            saveImage()
        }
        Handler().postDelayed({
            userNodeRef.child("$uid/firstName").setValue(firstName)
            userNodeRef.child("$uid/lastName").setValue(lastName)
            showDialogSendSuccess(requireContext())
        }, 3000)

    }

    private fun saveImage() {
        val storageReference = FirebaseStorage.getInstance().reference
        val userImagesRef = storageReference.child("avatar")
        val uniqueImageName = "image_${System.currentTimeMillis()}.jpg"
        val imageRef = userImagesRef.child(uniqueImageName)
        val inputStream = bitmapToInputStream(bitmap!!)
        val uploadTask = imageRef.putStream(inputStream)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    userNodeRef.child("$uid/imageUrl").setValue(imageUrl)
                }
            } else {
                println(task.exception)
                Toast.makeText(context, "Upload Image Unsuccessfully", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun bitmapToInputStream(bitmap: Bitmap): InputStream {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return ByteArrayInputStream(byteArray)
    }



}
