package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.ChatAdapter
import com.example.smartlamp.adapter.EnquiryAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.FragmentChatBinding
import com.example.smartlamp.databinding.FragmentEnquiryListBinding
import com.example.smartlamp.model.EnquiryDetailResponse
import com.example.smartlamp.model.EnquiryResponse
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants
import com.example.smartlamp.utils.Constants.ENQUIRY_ID
import com.example.smartlamp.utils.Constants.IS_ADMIN
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment(), OnItemSingleClickListener {
    lateinit var binding: FragmentChatBinding
    private val viewModel: ContactViewModel by activityViewModels()
    var enquiry = ArrayList<EnquiryDetailResponse.Data>()
    var enquiryId = -1
    var uid = -1

    private lateinit var chatAdapter: ChatAdapter

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatBinding.inflate(layoutInflater)
        val singleClick = SingleClickListener(this)
        sharedPref = SharedPref(context)
        uid = sharedPref.getInt(UID)

        binding.tvTitle.text = arguments?.getString("subject").toString()

        binding.ivBack.setOnClickListener(singleClick)
        binding.btnSend.setOnClickListener(singleClick)

        binding.swRefresh.setOnRefreshListener {
            onRefresh()
            binding.swRefresh.isRefreshing = false
        }

        binding.edtChat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.btnSend.isEnabled = !s.isNullOrEmpty()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSend.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
                binding.btnSend.isEnabled = !s.isNullOrEmpty()
            }
        })

        setObserb()

        return binding.root
    }

    private fun setObserb() {
        viewModel.enquiry.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                enquiry.clear()
                enquiry.addAll(it)
                enquiryId = it[0].enquiryId
                setUI()
            }
        }
    }


    private fun onRefresh() {
        viewModel.getEnquiry(uid)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        chatAdapter = ChatAdapter(requireContext(), enquiry)
        binding.rvChat.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(context)
            smoothScrollToPosition(enquiry.size - 1)
        }
        chatAdapter.notifyDataSetChanged()
    }

    private fun send() {
        apiInterface.sendEnquiry(
            hashMapOf(
                ENQUIRY_ID to enquiryId,
                IS_ADMIN to false,
                Constants.CONTENT to binding.edtChat.text.toString()
            )
        ).enqueue(object : Callback<SimpleApiResponse> {
            override fun onResponse(
                call: Call<SimpleApiResponse>,
                response: Response<SimpleApiResponse>
            ) {
                if (response.body()?.code == 200) {
                    viewModel.getEnquiryDetail(enquiryId)
                    binding.edtChat.setText("")
                } else {
                    Toast.makeText(context, response.body()?.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    override fun onItemClick(view: View) {
        when (view.id) {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.btn_send -> send()
        }
    }


}