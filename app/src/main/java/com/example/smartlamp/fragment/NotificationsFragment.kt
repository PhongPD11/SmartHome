package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.R
import com.example.smartlamp.adapter.NotificationAdapter
import com.example.smartlamp.api.ApiInterface
import com.example.smartlamp.databinding.DialogNotificationBinding
import com.example.smartlamp.databinding.FragmentNotificationsBinding
import com.example.smartlamp.model.NotificationModel
import com.example.smartlamp.model.SimpleApiResponse
import com.example.smartlamp.utils.Constants.UID
import com.example.smartlamp.utils.ConvertTime
import com.example.smartlamp.utils.RecyclerTouchListener
import com.example.smartlamp.utils.SharedPref
import com.example.smartlamp.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    lateinit var binding: FragmentNotificationsBinding
    lateinit var bindingDialog: DialogNotificationBinding
//    lateinit var bindingDialogDeleteAll: DialogDeleteBinding
//    val viewModel: NotificationViewModel by activityViewModels()

    private val viewModel: HomeViewModel by activityViewModels()
    var uid = 0

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var apiInterface: ApiInterface

    private lateinit var notificationAdapter: NotificationAdapter
    var notificationsData = ArrayList<NotificationModel.Data>()
    private var deleteButtonVisible = false
    private var posSwiped = -1
    private var lastSwipe = -1
    private var moving = false

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE
        sharedPref = SharedPref(context)
        uid = sharedPref.getInt(UID)

        viewModel.getNotify(uid)

        binding.swRefresh.setOnRefreshListener {
            binding.swRefresh.isRefreshing = false
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getNotify(uid)
        }

        binding.rvNotification.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvNotification,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val id = notificationsData[position].id
                        showDialog(context!!, position)
                        apiInterface.readNotification(id)
                            .enqueue(object : Callback<SimpleApiResponse> {
                                override fun onResponse(
                                    call: Call<SimpleApiResponse>,
                                    response: Response<SimpleApiResponse>
                                ) {
                                    if (response.body()?.data != null) {
                                        viewModel.getNotify(uid)
                                    }
                                }

                                override fun onFailure(
                                    call: Call<SimpleApiResponse>,
                                    t: Throwable
                                ) {
                                    t.printStackTrace()
                                }
                            })
                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )

        viewModel.notifications.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            if (!it.isNullOrEmpty()) {
                notificationsData.clear()
                binding.tvNotExistNotify.visibility = View.GONE
                notificationsData.addAll(it)
                notificationAdapter.notifyDataSetChanged()
            } else {
                binding.tvNotExistNotify.visibility = View.VISIBLE
            }
        }

        setUI()

        return binding.root
    }

    private fun showDialog(context: Context, position: Int) {
        val dialog = BottomSheetDialog(context, R.style.CustomDialogTheme)
        bindingDialog = DialogNotificationBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        val window = dialog.window
        val params = window?.attributes
        val dialogData = notificationsData[position]

        params?.gravity = Gravity.BOTTOM
        window?.attributes = params

        bindingDialog.tvTitleDialog.text = dialogData.title
        bindingDialog.tvContentDialog.text = dialogData.content
        bindingDialog.tvTimeDialog.text =
            ConvertTime.formatDate(dialogData.createAt, "dd MMM yyyy - HH:mm")
        bindingDialog.tvContentDialog.movementMethod = ScrollingMovementMethod()
        dialog.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        notificationAdapter = NotificationAdapter(requireContext(), notificationsData)
        binding.rvNotification.apply {
            adapter = notificationAdapter
            layoutManager = LinearLayoutManager(context)
        }
        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    moving = false
                    val position = viewHolder.layoutPosition
                    if (lastSwipe != -1 && lastSwipe != position)
                        notificationAdapter.notifyItemChanged(lastSwipe)
                    lastSwipe = position
                }

                override fun onChildDraw(
                    c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
                ) {
                    posSwiped = viewHolder.adapterPosition
                    val view = viewHolder.itemView
                    val paint = Paint()
                    paint.color = resources.getColor(R.color.red)
                    paint.textSize = 30f
                    paint.isAntiAlias = true
                    val sizeButton = view.right.toFloat() - (view.right.toFloat() / 6f)
                    if (dX < -sizeButton/2f) {
                        val deleteButtonLeft = view.right - (view.right / 6f)
                        val deleteButtonTop = view.top.toFloat()
                        val deleteButtonRight = view.right.toFloat() - view.paddingRight
                        val deleteButtonBottom = view.bottom.toFloat()
                        val radius = 0f
                        var deleteButtonDelete = RectF(
                            deleteButtonLeft,
                            deleteButtonTop,
                            deleteButtonRight,
                            deleteButtonBottom
                        )
                        c.drawRoundRect(deleteButtonDelete, radius, radius, paint)
                        paint.color = resources.getColor(R.color.white)
                        val textButton = "Delete"
                        val rect = Rect()
                        paint.getTextBounds(textButton, 0, textButton.length, rect)
                        c.drawText(
                            "Delete",
                            deleteButtonDelete.centerX() - rect.width() / 2f,
                            deleteButtonDelete.centerY() + rect.height() / 2f,
                            paint
                        )
                    }

                    if (dX < -sizeButton/2f) {
                        deleteButtonVisible = true
                        moving = false
                    } else {
                        deleteButtonVisible = false
                        moving = false
                    }
//                    println("Delete $deleteButtonVisible --- dX: $dX - delBtn: $deleteButtonLeft")

                    if (deleteButtonVisible)
                        clickDeleteButtonListener(recyclerView, viewHolder, posSwiped)
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX / 6f,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvNotification)
        notificationAdapter.notifyDataSetChanged()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickDeleteButtonListener(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        posSwiped: Int
    ) {
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(posSwiped)
        val item = viewHolder?.itemView

        item?.let {
            recyclerView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_UP && event.y > item.y && event.y < item.y + item.height
                    && event.x > item.x + item.width && !moving
                ) {
                    if (deleteButtonVisible) {
                        deleteItem(posSwiped)
                        deleteButtonVisible = false
                    }
                }
                false
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteItem(position: Int) {
        if (position < notificationsData.size) {
            apiInterface.deleteNotification(notificationsData[position].id).enqueue(object :
                Callback<SimpleApiResponse> {
                override fun onResponse(
                    call: Call<SimpleApiResponse>,
                    response: Response<SimpleApiResponse>
                ) {
                    if (response.body()?.data == null) {
                        Toast.makeText(
                            requireContext(),
                            response.body()?.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.getNotify(uid)
                    }
                }

                override fun onFailure(call: Call<SimpleApiResponse>, t: Throwable) {
                }
            })
            notificationsData.removeAt(position)
            notificationAdapter.notifyDataSetChanged()
        }
    }


}