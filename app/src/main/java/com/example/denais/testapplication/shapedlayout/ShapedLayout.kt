package com.example.denais.testapplication.shapedlayout

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.renderscript.Allocation
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript
import android.graphics.Color.BLACK
import android.graphics.Color.TRANSPARENT
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.renderscript.Element
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.denais.testapplication.R


abstract class ShapedLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout (context, attrs, defStyleAttr) {

    private var shadow: Bitmap? = null
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG)
    private val shadowRadius = 10f
    private var shadowOffset = 0f

    private val foreground: ShapedView
    private val shadowView: ImageView

    var isShadowDirty = true

    var containerReady = false

    init {
        shadowPaint.colorFilter = PorterDuffColorFilter(BLACK, PorterDuff.Mode.SRC_IN)
        shadowPaint.alpha = 200

        val arr = context.obtainStyledAttributes(attrs, R.styleable.ShapedLayout)
        val elevation = arr.getDimensionPixelSize(R.styleable.ShapedLayout_elevation, 0)
        shadowOffset = arr.getDimension(R.styleable.ShapedLayout_elevation, 0f)

        shadowView = ImageView(context).apply {
            layoutParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        addView(shadowView)

        foreground = ShapedView(context).apply {
            shaper = getShaper()
        }
        val foregroundParams = ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                .apply {
                    leftMargin = elevation
                    topMargin = elevation
                    rightMargin = elevation
                    bottomMargin = elevation
                }
        addView(foreground, foregroundParams)

        containerReady = true

        setWillNotDraw(false)
        arr.recycle()

    }

    abstract fun getShaper(): Shaper

    final override fun addView(child: View?) {
        if (containerReady) {
            foreground.addView(child)
        } else {
            super.addView(child)
        }
    }

    override fun addView(child: View?, index: Int) {
        if (containerReady) {
            foreground.addView(child, index)
        } else {
            super.addView(child, index)
        }
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (containerReady) {
            foreground.addView(child, params)
        } else {
            super.addView(child, params)
        }
    }

    override fun addView(child: View?, width: Int, height: Int) {
        if (containerReady) {
            foreground.addView(child, width, height)
        } else {
            super.addView(child, width, height)
        }
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (containerReady) {
            foreground.addView(child, index, params)
        } else {
            super.addView(child, index, params)
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        generateShadow()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        isShadowDirty = true
    }

    private fun generateShadow() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && isShadowDirty) {

            if (shadow == null) {
                shadow = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
            } else {
                shadow?.eraseColor(TRANSPARENT)
            }
            val c = Canvas(shadow)
            val shadowPath = getShaper().getPath(foreground.width, foreground.height).apply {
                offset(shadowOffset, shadowOffset)
            }
            c.drawPath(shadowPath, shadowPaint)

            val rs = RenderScript.create(context)
            val blur = ScriptIntrinsicBlur.create(rs, Element.U8(rs))
            val input = Allocation.createFromBitmap(rs, shadow)
            val output = Allocation.createTyped(rs, input.type)
            blur.setRadius(shadowRadius)
            blur.setInput(input)
            blur.forEach(output)
            output.copyTo(shadow)
            input.destroy()
            output.destroy()
            blur.destroy()

            shadowView.setImageBitmap(shadow)

            isShadowDirty = false
        }
    }

    companion object {
        val TAG = "ShapedView"
    }

}