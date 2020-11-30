package com.goharsha.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.doOnEnd
import java.util.*

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textRect = Rect()

    private var animator: ValueAnimator? = null
    private var progress: Float = 0f

    var buttonState = ButtonState.DOWNLOAD
        set(value) {
            if (field == value) {
                return
            }

            when (value) {
                ButtonState.DOWNLOAD -> {
                    animator?.cancel()
                    progress = 0f
                    animator = null
                }

                ButtonState.DOWNLOADING,
                ButtonState.LOADING ->
                    animator = ValueAnimator.ofFloat(0f, 1f).apply {
                        addUpdateListener {
                            progress = animatedValue as Float
                            invalidate()
                        }

                        if (value == ButtonState.LOADING) {
                            doOnEnd { buttonState = ButtonState.DOWNLOAD }
                        } else {
                            repeatMode = ValueAnimator.REVERSE
                            repeatCount = ValueAnimator.INFINITE
                        }

                        duration = 3000
                        start()
                    }
            }

            field = value
            invalidate()
        }

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.colorPrimary)
    }

    private val backgroundProgressPaint = Paint().apply {
        isAntiAlias = true
        color = context.getColor(R.color.colorPrimaryDark)
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val progressArcPaint = Paint().apply {
        isAntiAlias = true
    }

    private val buttonText get() = context.getString(buttonState.text).toUpperCase(Locale.ROOT)

    init {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.LoadingButton, defStyleAttr, 0)

        with(textPaint) {
            color = typedArray.getColor(R.styleable.LoadingButton_android_textColor, Color.WHITE)
            textSize = typedArray.getDimensionPixelSize(R.styleable.LoadingButton_android_textSize, 10.sp.toInt()).toFloat()
        }

        progressArcPaint.color = typedArray.getColor(R.styleable.LoadingButton_progressArcColor, Color.WHITE)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cornerRadius = 10.dp
        val backgroundWidth = measuredWidth.toFloat()
        val backgroundHeight = measuredHeight.toFloat()

        textPaint.getTextBounds(buttonText, 0, buttonText.length, textRect)
        canvas.drawRoundRect(0f, 0f, backgroundWidth, backgroundHeight, cornerRadius, cornerRadius, backgroundPaint)

        if (buttonState != ButtonState.DOWNLOAD) {
            var progressVal = progress * backgroundWidth
            canvas.drawRoundRect(0f, 0f, progressVal, backgroundHeight, cornerRadius, cornerRadius, backgroundProgressPaint)

            val arcDiameter = cornerRadius * 2
            val arcRectSize = measuredHeight.toFloat() - paddingBottom.toFloat() - arcDiameter

            progressVal = progress * 360f
            canvas.drawArc(
                    paddingStart + arcDiameter,
                    paddingTop.toFloat() + arcDiameter,
                    arcRectSize,
                    arcRectSize,
                    0f,
                    progressVal,
                    true,
                    progressArcPaint
            )
        }

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