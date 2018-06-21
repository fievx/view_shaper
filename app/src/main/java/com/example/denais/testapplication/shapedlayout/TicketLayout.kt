package com.example.denais.testapplication.shapedlayout

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import com.example.denais.testapplication.R
import android.renderscript.Allocation
import android.renderscript.ScriptIntrinsicBlur
import android.renderscript.RenderScript
import android.graphics.Color.BLACK
import android.graphics.Color.TRANSPARENT
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.renderscript.Element
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import org.w3c.dom.Attr


class TicketLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout (context, attrs, defStyleAttr) {

    private var shadow: Bitmap? = null
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG)
    private val shadowRadius = 10f

    private val foreground: TicketShapedView
    private val shadowView: ImageView

    var isShadowDirty = true

    var containerReady = false

    init {
        shadowPaint.colorFilter = PorterDuffColorFilter(BLACK, PorterDuff.Mode.SRC_IN)
        shadowPaint.alpha = 200 // 20%

        shadowView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                topMargin = 10
            }
        }
        addView(shadowView)

        foreground = TicketShapedView(context).apply {
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        addView(foreground)

        containerReady = true

        setWillNotDraw(false)

    }

    override fun addView(child: View?) {
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
            foreground.addView(child, getForegroundLayoutParams(params = params as LayoutParams))
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
            foreground.addView(child, index, getForegroundLayoutParams(params = params as LayoutParams))
        } else {
            super.addView(child, index, params)
        }
    }

    private fun getForegroundLayoutParams(params: LayoutParams): ViewGroup.LayoutParams{
        //we get the proper AttributeSet
        val id = params.id
        val attrs = attrMap[id]
        val arr = context.obtainStyledAttributes(attrs, R.styleable.TicketLayout)
        if (arr.hasValue(R.styleable.TicketLayout_android_layout_height) && arr.hasValue(R.styleable.TicketLayout_android_layout_width)) {
            return foreground.generateLayoutParams(attrMap[id])
        } else {
            return generateDefaultLayoutParams()
        }
    }

    val attrMap = HashMap<Int, AttributeSet?>()

    override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
        attrMap[getIdFromAttrs(context, attrs)] = attrs
        return LayoutParams(context, attrs)
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
            c.drawPath(foreground.shapePath, shadowPaint)

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

    override fun setRotationY(rotationY: Float) {
        Log.d(TAG, "Current rotation = $rotationY")
        super.setRotationY(rotationY)
    }

    companion object {
        val TAG = "TicketView"

        fun getIdFromAttrs(c: Context, attrs: AttributeSet):Int{
            val attrsArray = intArrayOf(android.R.attr.id)
            val arr = c.obtainStyledAttributes(attrs, attrsArray)
            val id = arr.getResourceId(0, View.NO_ID)
            arr.recycle()
            return id
        }
    }

    inner class LayoutParams(c: Context, attrs: AttributeSet) : FrameLayout.LayoutParams(c, attrs) {
        val id: Int = getIdFromAttrs(c, attrs)

    }
}