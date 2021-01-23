package com.homework.hw4_2_kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

interface ICustomListener {
    fun notification(colorOfClick: Int, message: String?, isSnackBar: Boolean)
}

class CustomButtonView(context: Context, attrs: AttributeSet) :
        View(context, attrs) {
    var customListener: ICustomListener? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var colorN1 = ContextCompat.getColor(context, R.color.color_1)
    private var colorN2 = ContextCompat.getColor(context, R.color.color_2)
    private var colorN3 = ContextCompat.getColor(context, R.color.color_3)
    private var colorN4 = ContextCompat.getColor(context, R.color.color_4)
    private var colorN5 = ContextCompat.getColor(context, R.color.color_5)
    private var colorN6 = ContextCompat.getColor(context, R.color.color_6)
    private var colorN7 = ContextCompat.getColor(context, R.color.color_7)
    private var colorN8 = ContextCompat.getColor(context, R.color.color_8)
    private var colorN9 = ContextCompat.getColor(context, R.color.color_9)
    private var colorN10 = ContextCompat.getColor(context, R.color.color_10)
    private var colorN11 = ContextCompat.getColor(context, R.color.color_11)
    private var colorN12 = ContextCompat.getColor(context, R.color.color_12)
    private var colorN13 = ContextCompat.getColor(context, R.color.color_13)
    private var colorN14 = ContextCompat.getColor(context, R.color.color_14)
    private var colorN15 = ContextCompat.getColor(context, R.color.color_15)
    private var colorN16 = ContextCompat.getColor(context, R.color.color_16)
    private var colorN17 = ContextCompat.getColor(context, R.color.color_17)
    private var colorN18 = ContextCompat.getColor(context, R.color.color_18)
    private var colorN19 = ContextCompat.getColor(context, R.color.color_19)
    private var colorN20 = ContextCompat.getColor(context, R.color.color_20)
    private var colorOfFirstPart = COLOR_OF_FIRST_PART
    private var colorOfSecondPart = COLOR_OF_SECOND_PART
    private var colorOfThirdPart = COLOR_OF_THIRD_PART
    private var colorOfFourthPart = COLOR_OF_FOURTH_PART
    private var colorOfCenter = COLOR_OF_CENTER
    private var colorOfBorder = COLOR_OF_BORDER
    private val myColors = arrayListOf(
            colorN1, colorN2, colorN3, colorN4,
            colorN5, colorN6, colorN7, colorN8, colorN9, colorN10, colorN11,
            colorN12,colorN13,colorN14,colorN15,colorN16,colorN17,colorN18,
            colorN19,colorN20
    )
    companion object {
        private const val COLOR_OF_FIRST_PART = Color.GREEN
        private const val COLOR_OF_SECOND_PART = Color.YELLOW
        private const val COLOR_OF_THIRD_PART = Color.BLUE
        private const val COLOR_OF_FOURTH_PART = Color.RED
        private const val COLOR_OF_CENTER = Color.WHITE
        private const val COLOR_OF_BORDER = Color.BLACK
        private const val WIDTH_BORDER = 1.0f
    }

    private var radiusOfClick = 0f
    private var xyOfClick: String? = null
    private var centerX: Float = 0.0f
    private var centerY: Float = 0.0f
    private var colorOfClick: Int = 0
    private var borderWidth = WIDTH_BORDER
    private var size = 620

    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomButtonView,
                0, 0)
        colorOfFirstPart = typedArray.getColor(R.styleable.CustomButtonView_colorFirstPart,
                COLOR_OF_FIRST_PART)
        colorOfSecondPart = typedArray.getColor(R.styleable.CustomButtonView_colorSecondPart,
                COLOR_OF_SECOND_PART)
        colorOfThirdPart = typedArray.getColor(R.styleable.CustomButtonView_colorThirdPart,
                COLOR_OF_THIRD_PART)
        colorOfFourthPart = typedArray.getColor(R.styleable.CustomButtonView_colorFourthPart,
                COLOR_OF_FOURTH_PART)
        colorOfCenter = typedArray.getColor(R.styleable.CustomButtonView_colorOfCenter,
                COLOR_OF_CENTER)
        colorOfBorder = typedArray.getColor(R.styleable.CustomButtonView_colorOfBorder,
                COLOR_OF_BORDER)
        borderWidth = typedArray.getDimension(R.styleable.CustomButtonView_widthOfBorder,
                WIDTH_BORDER)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        size = measuredHeight.coerceAtMost(measuredWidth)
        setMeasuredDimension(size, size)
        centerX = size / 2f
        centerY = size / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBorder(canvas)
        drawFirstPart(canvas)
        drawSecondPart(canvas)
        drawThirdPart(canvas)
        drawFourthPart(canvas)
        drawCenter(canvas)
    }

    private fun getRectF(): RectF = RectF(centerX - size / 2 + borderWidth,
            centerY - size / 2 + borderWidth, centerX + size / 2 - borderWidth,
            centerY + size / 2 - borderWidth)

    private fun drawBorder(canvas: Canvas) {
        paint.color = colorOfBorder
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
        canvas.drawCircle(centerX, centerY, size * 0.5f, paint)
    }

    private fun drawFirstPart(canvas: Canvas) {
        paint.color = colorOfFirstPart
        paint.style = Paint.Style.FILL
        canvas.drawArc(getRectF(), -90F, 90F, true, paint)
    }

    private fun drawSecondPart(canvas: Canvas) {
        paint.color = colorOfSecondPart
        paint.style = Paint.Style.FILL
        canvas.drawArc(getRectF(), 0F, 90F, true, paint)
    }

    private fun drawThirdPart(canvas: Canvas) {
        paint.color = colorOfThirdPart
        paint.style = Paint.Style.FILL
        canvas.drawArc(getRectF(), 90F, 90F, true, paint)
    }

    private fun drawFourthPart(canvas: Canvas) {
        paint.color = colorOfFourthPart
        paint.style = Paint.Style.FILL
        canvas.drawArc(getRectF(), 180F, 90F, true, paint)
    }

    private fun drawCenter(canvas: Canvas) {
        paint.color = colorOfCenter
        paint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, size * 0.15f, paint)
    }

    private fun changeColors() {
        colorOfFirstPart = myColors.random()
        colorOfSecondPart = myColors.random()
        colorOfThirdPart = myColors.random()
        colorOfFourthPart = myColors.random()
    }

    private fun firstPartColorChange() {
        colorOfFirstPart = myColors.random()
    }

    private fun secondPartColorChange() {
        colorOfSecondPart = myColors.random()
    }

    private fun thirdPartColorChange() {
        colorOfThirdPart = myColors.random()
    }

    private fun fourthPartColorChange() {
        colorOfFourthPart = myColors.random()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x - centerX
        val y = event.y - centerY
        radiusOfClick = sqrt(abs(x).pow(2) + abs(y).pow(2))
        if ((event.action == MotionEvent.ACTION_DOWN) && (radiusOfClick <= size * 0.15f)) {
            xyOfClick = "(x, y): (${x.toInt()}, ${y.toInt()})"
            changeColors()
            colorOfClick = colorN7
            invalidate()
        } else if ((event.action == MotionEvent.ACTION_DOWN) && (radiusOfClick <= size / 2)) {
            xyOfClick = "(x, y): (${x.toInt()}, ${y.toInt()})"
            if ((x > 0) && (y < 0)) {
                firstPartColorChange()
                colorOfClick = colorOfFirstPart
            }
            if ((x > 0) && (y > 0)) {
                secondPartColorChange()
                colorOfClick = colorOfSecondPart
            }
            if ((x < 0) && (y > 0)) {
                thirdPartColorChange()
                colorOfClick = colorOfThirdPart
            }
            if ((x < 0) && (y < 0)) {
                fourthPartColorChange()
                colorOfClick = colorOfFourthPart
            }
            invalidate()
        } else if (event.action == MotionEvent.ACTION_DOWN) {
            xyOfClick = "Нет попадания"
            colorOfClick = colorN13
            invalidate()
        }
        return super.onTouchEvent(event)
    }

    fun coordinates() = xyOfClick
    fun getColorOfClick() = colorOfClick

    }




