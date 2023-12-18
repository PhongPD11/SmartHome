package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.smartlamp.R
import com.example.smartlamp.adapter.ChatAdapter
import com.example.smartlamp.adapter.EnquiryAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.FragmentBorrowBinding
import com.example.smartlamp.databinding.FragmentChatBinding
import com.example.smartlamp.databinding.FragmentEnquiryListBinding
import com.example.smartlamp.model.EnquiryDetailResponse
import com.example.smartlamp.model.EnquiryResponse
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.ACTIVE_CODE
import com.example.smartlamp.utils.Constants.BOOK_ID
import com.example.smartlamp.utils.Constants.BORROW
import com.example.smartlamp.utils.Constants.ENQUIRY_ID
import com.example.smartlamp.utils.Constants.IS_ADMIN
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.utils.Utils
import com.example.smartlamp.viewmodel.ContactViewModel
import com.example.smartlamp.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
private const val CAMERA_REQUEST_CODE = 101

@AndroidEntryPoint
class BorrowFragment : Fragment(), OnItemSingleClickListener {
    lateinit var binding: FragmentBorrowBinding
    private lateinit var codeScanner: CodeScanner

//    private val viewModel: ContactViewModel by activityViewModels()

    var from = ""
    var bookId = -1L
    var uid = -1

    private lateinit var chatAdapter: ChatAdapter

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBorrowBinding.inflate(layoutInflater)
        val singleClick = SingleClickListener(this)
        sharedPref = SharedPref(context)
        from = arguments?.getString("from").toString()
        bookId = arguments?.getLong(BOOK_ID)!!.toLong()
        uid = sharedPref.getInt(UID)

        if (from != BORROW) {
            binding.tvTitle.text = resources.getString(R.string.return_book)
        }

        setupPermission()
        codeScanner(requireContext())

        binding.ivBack.setOnClickListener(singleClick)

        return binding.root
    }

    private fun codeScanner(context: Context){
        codeScanner = CodeScanner(context, binding.scannerView)
        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                activity?.runOnUiThread {
                    action(it.text)
                }
            }
        }

        binding.scannerView.setOnClickListener{
            codeScanner.startPreview()
        }

    }

    private fun action(text: String){
        val parts = text.split(",")

        val bookIdQR = parts[0]
        val jwtQR = parts[1]
        val token = "Bearer $jwtQR"

        if (bookIdQR == bookId.toString()) {
            if (from == BORROW) {
                apiInterface.borrowBook(token, bookId, uid).enqueue(object : Callback<SimpleApiResponse> {
                    override fun onResponse(
                        call: Call<SimpleApiResponse>,
                        response: Response<SimpleApiResponse>
                    ) {
                        if (response.body()?.code == 200) {
                            viewModel.getUserBook(uid)
                            Utils.showSimpleDialog(context!!, "Borrow successfully!", "", findNavController())
                        } else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
                codeScanner.releaseResources()
            } else {
                apiInterface.returnBook(token, bookId, uid).enqueue(object : Callback<SimpleApiResponse> {
                    override fun onResponse(
                        call: Call<SimpleApiResponse>,
                        response: Response<SimpleApiResponse>
                    ) {
                        if (response.body()?.code == 200) {
                            viewModel.getUserBook(uid)
                            Utils.showSimpleDialog(context!!, "Return successfully!","", findNavController())
                        } else {
                            Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
                codeScanner.releaseResources()
            }
        } else {
            Toast.makeText(context, "BookId mismatch!", Toast.LENGTH_SHORT).show()
        }

    }




    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }


    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.iv_back -> findNavController().popBackStack()
        }
    }

    private fun setupPermission(){
        val permission = ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE-> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "You need the camera permission to be able to user QR Scanner", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}