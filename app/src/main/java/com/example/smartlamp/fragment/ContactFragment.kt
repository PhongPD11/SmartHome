package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.smartlamp.R
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.DialogSuccessBinding
import com.example.smartlamp.databinding.FragmentContactBinding
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.EMAIL
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap
import javax.inject.Inject

@AndroidEntryPoint
class ContactFragment : Fragment(), OnItemSingleClickListener {
    lateinit var binding: FragmentContactBinding
    private var isValidate = false
    private val viewModel: ContactViewModel by activityViewModels()

    private var uid = -1

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactBinding.inflate(layoutInflater)
        val singleClick = SingleClickListener(this)

        sharedPref = SharedPref(context)
        uid = sharedPref.getInt(UID)

        binding.ivBack.setOnClickListener(singleClick)
        binding.btnSend.setOnClickListener(singleClick)

        binding.etEmail.setText(sharedPref.getString(EMAIL).toString())

        binding.etDesc.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                view.parent.requestDisallowInterceptTouchEvent(false)
            }
            return@setOnTouchListener false
        }

        binding.etSubject.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty()) {
                binding.laySubject.error = null
                binding.laySubject.isErrorEnabled = false
                isValidate = true

            } else {
                isValidate = false
            }
        }
        binding.etDesc.doOnTextChanged { text, _, _, _ ->

            if (!text.isNullOrEmpty()) {
                binding.layDesc.error = null
                binding.layDesc.isErrorEnabled = false
                isValidate = true
            } else {
                isValidate = false
            }
        }

        return binding.root
    }

    private fun send() {
        if (isValidate) {
            apiInterface.createEnquiry(
                hashMapOf(
                    UID to uid,
                    Constants.TITLE to binding.etSubject.text.toString(),
                    Constants.CONTENT to binding.etDesc.text.toString()
                )
            ).enqueue(object : Callback<SimpleApiResponse> {
                override fun onResponse(
                    call: Call<SimpleApiResponse>,
                    response: Response<SimpleApiResponse>
                ) {
                    if (response.body()?.code == 200) {
                        showDialogSendSuccess(context!!)
                        viewModel.getEnquiry(uid)
                    } else {
                        Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
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

        bindingDialog.tvTitle.text = getString(R.string.send_enquiry_successfully)

        bindingDialog.btnYes.setOnClickListener {
            dialog.dismiss()
            findNavController().popBackStack()
        }
        dialog.show()
    }

    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.btn_send -> send()
        }
    }
}