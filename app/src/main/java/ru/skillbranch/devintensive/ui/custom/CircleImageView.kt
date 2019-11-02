package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import ru.skillbranch.devintensive.R
import android.graphics.Shader
import android.graphics.BitmapShader
import android.graphics.RectF
import android.util.Log


class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ImageView(context, attrs, defStyleAttr) {

    companion object {
        @ColorInt private const val DEFAULT_BORDER_COLOR = Color.WHITE
        @Dimension(unit = Dimension.DP) private const val DEFAULT_BORDER_WIDTH = 2f

    }

    private var mStrokePaint: Paint
    private var mBorderColor = DEFAULT_BORDER_COLOR
    private var mBorderWidth = DEFAULT_BORDER_WIDTH


    private val mBitmapPaint = Paint()
    private var mBitmap = this.drawable.toBitmap()
    private var mBitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

    private val mStrokeBounds = RectF()
    private val mBitmapDrawBounds = RectF()
    private val mShaderMatrix = Matrix()

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            mBorderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            mBorderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            a.recycle()
        }

        mBitmapPaint.shader = mBitmapShader
        mStrokePaint = Paint().apply {
            isAntiAlias = true
            color = getBorderColor()
            strokeWidth = getBorderWidth().toFloat()
            style = Paint.Style.STROKE
        }
        setupBitmap()
    }

    fun setupBitmap() {
        mBitmapShader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.shader = mBitmapShader
        updateBitmapSize()
    }

    private fun updateBitmapSize() {
        val dx: Float
        val dy: Float
        val scale: Float

        // scale up/down with respect to this view size and maintain aspect ratio
        // translate bitmap position with dx/dy to the center of the image
        if (mBitmap.width < mBitmap.height) {
            scale = mBitmapDrawBounds.width() / mBitmap.width.toFloat()
            dx = mBitmapDrawBounds.left
            dy = mBitmapDrawBounds.top - mBitmap.height * scale / 2f + mBitmapDrawBounds.width() / 2f
        } else {
            scale = mBitmapDrawBounds.height() / mBitmap.height.toFloat()
            dx = mBitmapDrawBounds.left - mBitmap.width * scale / 2f + mBitmapDrawBounds.width() / 2f
            dy = mBitmapDrawBounds.top
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(dx, dy)
        mBitmapShader.setLocalMatrix(mShaderMatrix)
    }

    @Dimension fun getBorderWidth(): Int {
        return mBorderWidth.toInt()
    }

    fun setBorderWidth(@Dimension dp: Int) {
        mBorderWidth = dp.toFloat()
    }


    fun getBorderColor(): Int {
        return mBorderColor
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        mBorderColor = ContextCompat.getColor(context, colorId)
    }

    fun setBorderColor(hex:String) {
        mBorderColor = Color.parseColor(hex)
    }

    override fun onDraw(canvas: Canvas) {
        drawBitmap(canvas)
        drawBorder(canvas)
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawOval(mStrokeBounds, mStrokePaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val halfStrokeWidth = mStrokePaint.strokeWidth / 2f
        updateCircleDrawBounds(mBitmapDrawBounds)
        mStrokeBounds.set(mBitmapDrawBounds)
        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth)

        updateBitmapSize()
    }


    fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()

        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }

        val diameter = contentWidth.coerceAtMost(contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun drawBitmap(canvas: Canvas) {
        canvas.drawOval(mBitmapDrawBounds, mBitmapPaint)
    }

}