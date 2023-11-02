package com.example.smartlamp.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.smartlamp.R
import com.example.smartlamp.databinding.ItemTextViewParallelLinesBinding


class TextViewParallelLines(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private var binding: ItemTextViewParallelLinesBinding? = null

    init {
        init(context, attrs)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        binding?.subTitle?.setOnClickListener {
            println("VCL")
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ItemTextViewParallelLinesBinding.inflate(inflater, this, true)
        var typedArray: TypedArray? = null
        try {
            val defaultTextSize = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 14F, resources.displayMetrics
            ).toInt()

            typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewTwoLines)
            binding?.title?.text = typedArray.getString(R.styleable.TextViewTwoLines_titleLabel)
            binding?.subTitle?.text =
                typedArray.getString(R.styleable.TextViewTwoLines_subTitle)

            val titleTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TextViewTwoLines_titleTextSize,
                defaultTextSize
            )
            val subTitleTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TextViewTwoLines_subTitleTextSize,
                defaultTextSize
            )
            binding?.title?.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())
            binding?.subTitle?.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTitleTextSize.toFloat())

            val titleTextStyle =
                typedArray.getInt(R.styleable.TextViewTwoLines_titleStyle, Typeface.NORMAL)
            val subTitleTextStyle =
                typedArray.getInt(R.styleable.TextViewTwoLines_subTitleStyle, Typeface.NORMAL)
            val titleTypeface = Typeface.defaultFromStyle(titleTextStyle)
            val subTitleTypeface = Typeface.defaultFromStyle(subTitleTextStyle)
            binding?.title?.typeface = titleTypeface
            binding?.subTitle?.typeface = subTitleTypeface

        } finally {
            typedArray?.recycle()
        }

        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        binding?.root?.layoutParams = layoutParams
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        binding?.root?.measure(
            MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
            heightMeasureSpec
        )
    }
    fun setGravity(gravity: Int){
        binding?.subTitle?.gravity = gravity
    }

    fun setText(content: String?) {
        binding?.subTitle?.text = content
    }

    fun setTitleText(content: String?) {
        binding?.title?.text = content
    }

    fun setTextColor(color: Int) {
        binding?.subTitle?.setTextColor(color)
    }

    fun setTitleSize(size: Float) {
        binding?.title?.textSize = size
    }

    fun setTextBold() {
        binding?.subTitle?.setTypeface(binding?.subTitle?.typeface, Typeface.BOLD)
    }
}