package com.example.smartlamp.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Bundle
import android.os.Handler
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smartlamp.R
import com.example.smartlamp.adapter.NotificationAdapter
import com.example.smartlamp.databinding.FragmentNotificationsBinding
import com.example.smartlamp.model.NotificationModel
import com.example.smartlamp.utils.RecyclerTouchListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : Fragment(){
    lateinit var binding: FragmentNotificationsBinding
//    lateinit var bindingDialog: DialogNotificationBinding
//    lateinit var bindingDialogDeleteAll: DialogDeleteBinding
//    val viewModel: NotificationViewModel by activityViewModels()
    private lateinit var notificationAdapter: NotificationAdapter
    var notificationsData = ArrayList<NotificationModel.Data>()
    private var deleteButtonVisible = false
    private var posSwiped = -1
    private var lastSwipe = -1
    private var moving = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotificationsBinding.inflate(layoutInflater)

//        showDialogDeleteAll(context!!)


        binding.swRefresh.setOnRefreshListener {
            binding.swRefresh.isRefreshing = false
        }

        binding.rvNotification.addOnItemTouchListener(
            RecyclerTouchListener(activity,
                binding.rvNotification,
                object : RecyclerTouchListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {

                    }

                    override fun onLongItemClick(view: View?, position: Int) {
                    }
                })
        )

        return binding.root
    }

//    private fun noNotify() {
//        if (notificationsData.isEmpty()) {
//            binding.tvNotExistNotify.visibility = View.VISIBLE
//            binding.tvClear.visibility = View.GONE
//        } else {
//            binding.tvNotExistNotify.visibility = View.GONE
//            binding.tvClear.visibility = View.VISIBLE
//        }
//    }

//    private fun showDialog(context: Context, position: Int) {
//        val dialog = BottomSheetDialog(context, R.style.CustomBottomSheetDialogTheme)
//        bindingDialog = DialogNotificationBinding.inflate(layoutInflater)
//        dialog.setContentView(bindingDialog.root)
//        val window = dialog.window
//        val params = window?.attributes
//        val dialogData = notificationsData[position]
//
//        params?.gravity = Gravity.BOTTOM
//        window?.attributes = params
//
//        bindingDialog.tvTitleDialog.text = dialogData.title
//        bindingDialog.tvContentDialog.text = dialogData.content
//        bindingDialog.tvTimeDialog.text =
//            ConvertTime.formatDate(dialogData.time, "dd MMM yyyy - HH:mm:ss")
//        bindingDialog.tvContentDialog.movementMethod = ScrollingMovementMethod()
//        dialog.show()
//    }

//    private fun showDialogDeleteAll(context: Context) {
//        val dialog = Dialog(context, R.style.CustomCenterDialogTheme)
//        bindingDialogDeleteAll = DialogDeleteBinding.inflate(layoutInflater)
//        dialog.setContentView(bindingDialogDeleteAll.root)
//        val window = dialog.window
//        val params = window?.attributes
//
//        params?.gravity = Gravity.CENTER
//        window?.attributes = params
//
//        bindingDialogDeleteAll.ivClose.setSingleClickListener {
//            dialog.dismiss()
//        }
//        bindingDialogDeleteAll.btnAccept.setSingleClickListener {
//            viewModel.deleteAllNotification()
//            dialog.dismiss()
//            onRefresh()
//        }
//        dialog.show()
//    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUI() {
        notificationAdapter = NotificationAdapter(requireContext())
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
                    deleteButtonVisible = true
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
                    val deleteButtonLeft = view.right - (view.right / 5f)
                    val deleteButtonTop = view.top.toFloat()
                    val deleteButtonRight = view.right.toFloat() - view.paddingRight
                    val deleteButtonBottom = view.bottom.toFloat()
                    val radius = 0f
                    val deleteButtonDelete = RectF(
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
                    if (dX <= -deleteButtonLeft) {
                        deleteButtonVisible = true
                        moving = false
                    } else {
                        deleteButtonVisible = false
                        moving = true
                    }

                    if (dX == 0.0f)
                        moving = false
                    if (deleteButtonVisible)
                        clickDeleteButtonListener(recyclerView, viewHolder, posSwiped)
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX / 5f,
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
//                        deleteItem(posSwiped)
                        deleteButtonVisible = false
                    }
                }
                false
            }
        }
    }

//    private fun deleteItem(position: Int) {
//        if (position < notificationsData.size) {
//            viewModel.deleteNotification(notificationsData[position].id)
//            notificationsData.removeAt(position)
//            notificationAdapter.notifyDataSetChanged()
//        }
//    }


}