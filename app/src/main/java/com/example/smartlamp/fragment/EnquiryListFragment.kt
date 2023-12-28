package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartlamp.R
import com.example.smartlamp.adapter.EnquiryAdapter
import com.example.smartlamp.databinding.FragmentEnquiryListBinding
import com.example.smartlamp.model.EnquiryResponse
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.viewmodel.ContactViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.tux.wallet.testnet.utils.OnItemSingleClickListener
import io.tux.wallet.testnet.utils.SingleClickListener
import javax.inject.Inject

@AndroidEntryPoint
class EnquiryListFragment : Fragment(), OnItemSingleClickListener {
    lateinit var binding :FragmentEnquiryListBinding
    private val viewModel: ContactViewModel by activityViewModels()
    private lateinit var enquiryAdapter: EnquiryAdapter
    var enquiryList = ArrayList<EnquiryResponse.Data>()
    var uid = -1

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEnquiryListBinding.inflate(layoutInflater)
        val singleClick = SingleClickListener(this)
        sharedPref = SharedPref(context)
        uid = sharedPref.getInt(UID)

        binding.ivBack.setOnClickListener(singleClick)
        binding.btnAdd.setOnClickListener(singleClick)

        binding.swRefresh.setOnRefreshListener {
            onRefresh()
            binding.swRefresh.isRefreshing = false
        }

        binding.rvEnquiry.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvEnquiry,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val args = Bundle()
                        viewModel.getEnquiryDetail(enquiryList[position].id)
                        args.putString("subject", enquiryList[position].title)
                        args.putString("status", enquiryList[position].status)
                        findNavController().navigate(R.id.navigation_enquiry_detail, args)
                    }
                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )

        setObserb()
        setUI()

        return binding.root
    }

    private fun setObserb() {
        viewModel.enquiryList.observe(viewLifecycleOwner) {
            if (it != null) {
                enquiryList.clear()
                enquiryList.addAll(it)
                setUI()
            }
        }
    }


    private fun onRefresh() {
        viewModel.getEnquiry(uid)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        if (enquiryList.isEmpty()) {
            binding.tvNotExistNotify.visibility = View.VISIBLE
        } else {
            enquiryAdapter = EnquiryAdapter(requireContext(), enquiryList)
            binding.rvEnquiry.apply {
                adapter = enquiryAdapter
                layoutManager = LinearLayoutManager(context)
            }
            enquiryAdapter.notifyDataSetChanged()
            binding.tvNotExistNotify.visibility = View.GONE
        }
    }

    private fun noItem() {
        if (enquiryList.isEmpty()) {
            binding.tvNotExistNotify.visibility = View.VISIBLE
        } else {
            binding.tvNotExistNotify.visibility = View.GONE
        }
    }

    override fun onItemClick(view: View) {
        when(view.id)
        {
            R.id.iv_back -> findNavController().popBackStack()
            R.id.btn_add -> findNavController().navigate(R.id.navigation_enquiry)
        }
    }


}