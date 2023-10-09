package io.tux.wallet.testnet.utils

import android.view.View

class SingleClickListener(
    private val onItemClickListener: OnItemSingleClickListener
    ) : View.OnClickListener {
    private val delay: Long = 500
    private var lastClickTime: Long = 0

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > delay) {
            lastClickTime = currentTime
            onItemClickListener.onItemClick(v)
        }
    }

    companion object{
        private const val delay: Long = 500
        fun View.setSingleClickListener(onClickListener: View.OnClickListener) {
            var lastClickTime: Long = 0
            this.setOnClickListener { view ->
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastClickTime > delay) {
                    lastClickTime = currentTime
                    onClickListener.onClick(view)
                }
            }
        }
    }

}
interface OnItemSingleClickListener {
    fun onItemClick(view: View)
}
