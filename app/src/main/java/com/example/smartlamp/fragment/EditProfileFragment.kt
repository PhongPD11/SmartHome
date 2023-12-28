package com.example.smartlamp.fragment

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.smartlamp.R
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentEditProfileBinding
import com.example.smartlamp.model.UserEditModel
import com.example.smartlamp.model.UserResponseModel
import com.example.smartlamp.utils.Constants.CLASS_ID
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.FULL_NAME
import com.example.smartlamp.utils.Constants.IMAGE_URL
import com.example.smartlamp.utils.Constants.LAST_NAME
import com.example.smartlamp.utils.Constants.MAJOR
import com.example.smartlamp.utils.Constants.NAME
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Validations
import com.example.smartlamp.viewmodel.AccountViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val accountViewModel: AccountViewModel by activityViewModels()
    private var isValidate = false
    private var fullName = ""
    private var firstName = ""
    private var lastName = ""
    private var classId = -1
    private var major = ""
    private var file: MultipartBody.Part? = null

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

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

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        binding.etEmail.setText(sharedPref.getString(EMAIL))
        binding.etFirstName.setText(sharedPref.getString(NAME))
        binding.etLastName.setText(sharedPref.getString(LAST_NAME))
        binding.etMajor.setText(sharedPref.getString(MAJOR))
        binding.etClassId.setText(sharedPref.getInt(CLASS_ID).toString())
        val options: RequestOptions = RequestOptions()
            .centerCrop()
            .placeholder(com.example.smartlamp.R.drawable.ic_user)
            .error(com.example.smartlamp.R.drawable.ic_user)

        Glide.with(this).load(sharedPref.getString(IMAGE_URL)).apply(options).into(binding.ivAvatar)

        binding.ivAvatar.setOnClickListener {
            openImageChooser()
        }

        return binding.root
    }

    private fun TextInputEditText.onText(): String {
        return text.toString().trim()
    }

    private fun sendUpdate() {
        val uid = sharedPref.getInt(UID)
        firstName = binding.etFirstName.onText()
        lastName = binding.etLastName.onText()
        fullName = "$lastName $firstName"
        major = binding.etMajor.onText()
        if (binding.etClassId.onText().isNotEmpty()) {
            classId = binding.etClassId.onText().toInt()
            saveProfile(uid, fullName, major, classId)
        } else {
            saveProfile(uid, fullName, major, null)
        }

        binding.progressBar.visibility = View.VISIBLE
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
        classID: Int?
    ) {

        val profile = UserEditModel(classID, fullName, major, uid)
        val model = Gson().toJson(profile).toString()
        val modelSend = model.toRequestBody("multipart/form-data".toMediaType())

        apiInterface.editProfile(file, modelSend).enqueue(object : Callback<UserResponseModel> {
            override fun onResponse(
                call: Call<UserResponseModel>,
                response: Response<UserResponseModel>
            ) {
                if (response.body()?.code == 200) {
                    val data = response.body()?.data
                    showDialogSendSuccess(requireContext())
                    localSaveString(NAME, firstName)
                    localSaveString(LAST_NAME, lastName)
                    localSaveString(FULL_NAME, fullName)
                    if (classId != -1) {
                        sharedPref.putInt(CLASS_ID, classId)
                    }
                    localSaveString(MAJOR, major)
                    data?.imageUrl?.let { sharedPref.putString(IMAGE_URL, it) }

                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponseModel>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun localSaveString(key: String, data: String){
        if (data.isNotEmpty()){
            sharedPref.putString(key,data)
        }
    }

    private fun showAvatar(url: String) {
        val options = RequestOptions().placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round).skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.ALL).override(500, 500)
        var requestBuilder = Glide.with(requireContext()).load(url).apply(options)
        requestBuilder = requestBuilder.apply(options.transform(FitCenter(), RoundedCorners(20)))
        requestBuilder.into(binding.ivAvatar)
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            resultLauncher.launch(it)
        }
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data.let {
                    showAvatar(it.toString())
                    if (it != null) {
                        getPath(requireContext(), it)
                    }
                }
            }
        }

    private fun scaleImageBitmap(scaleSize: Float, bitmapResource: Bitmap): Bitmap {
        return Bitmap.createScaledBitmap(
            bitmapResource,
            (bitmapResource.width * scaleSize).toInt(),
            (bitmapResource.height * scaleSize).toInt(),
            true
        )
    }

    private fun getPath(context: Context, uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileImage = File(context.cacheDir, getFileName(context.contentResolver, uri))

        try {
            val outputStream = FileOutputStream(fileImage)
            inputStream?.copyTo(outputStream)
            outputStream.close()

            val compressedImageFile = compressImage(fileImage)
            if (compressedImageFile != null) {
                file = addFileImage(compressedImageFile, "file", compressedImageFile.name)
            }
        } catch (e: Exception) {
            file = null
        } finally {
            inputStream?.close()
        }
    }

    private fun addFileImage(
        file: File,
        nameImage: String,
        nameFile: String,
    ): MultipartBody.Part {
        val body = file.asRequestBody("file/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(nameImage, nameFile, body)
    }

    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var name = ""
        val returnCursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        returnCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }

    private fun compressImage(file: File): File? {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val scaledBitmap = scaleImageBitmap(0.7f, bitmap)
        val compressedBitmap = compressBitmap(scaledBitmap)
        val compressedFile = File(file.parentFile, "compressed_${file.name}")

        return try {
            val outputStream = FileOutputStream(compressedFile)
            compressedBitmap?.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            outputStream.close()
            println("Size Image ${compressedFile.length()}")
            compressedFile
        } catch (e: Exception) {
            null
        }
    }

    private val MAX_FILE_SIZE_KB = 512
    private fun compressBitmap(bitmap: Bitmap): Bitmap? {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        var quality = 100
        while (stream.toByteArray().size / 1028 > MAX_FILE_SIZE_KB && quality >= 0) {
            stream.reset()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            quality -= 10
        }
        val compressedBitmap = BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size())
        stream.close()
        return compressedBitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        accountViewModel.updateRes.value = null
    }

}
