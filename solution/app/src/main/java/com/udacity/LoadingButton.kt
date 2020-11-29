package com.udacity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import java.util.*

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var buttonState: ButtonState = ButtonState.Completed
    private val textRect = Rect()

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.colorPrimary)
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.white)
        textSize = 16.sp
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val buttonText: String
        get() = when (buttonState) {
            is ButtonState.Completed,
            is ButtonState.Clicked -> context.resources.getString(R.string.download)
            is ButtonState.Loading -> context.resources.getString(R.string.button_loading)
        }.toUpperCase(Locale.ROOT)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), 10.dp, 10.dp, backgroundPaint)
        textPaint.getTextBounds(buttonText, 0, buttonText.length, textRect)

        val centerX = measuredWidth.toFloat() / 2
        val centerY = measuredHeight.toFloat() / 2 - textRect.centerY()

        canvas.drawText(buttonText, centerX, centerY, textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minW: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minW, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0)

        setMeasuredDimension(w, h)
    }
}