package com.example.denais.viewshaper.shapedlayout

import android.content.Context


import android.graphics.*
import android.util.AttributeSet
import android.graphics.Color.BLACK
import android.graphics.Color.TRANSPARENT
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.denais.viewshaper.R
abstract class ShapedLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout (context, attrs, defStyleAttr) {

    private var shadow: Bitmap? = null
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG)
    private val shadowRadius = 10f
    private var shadowOffset = 0f

    private val foreground: ShapedView
    private val shadowView: ImageView

    var isShadowDirty = true

    var containerReady = false

    val viewsMap = mutableMapOf<View?, ViewGroup.LayoutParams?>()

    init {
        shadowPaint.colorFilter = PorterDuffColorFilter(BLACK, PorterDuff.Mode.SRC_IN)
        shadowPaint.alpha = 200

        val arr = context.obtainStyledAttributes(attrs, R.styleable.ShapedLayout)
        val elevation = arr.getDimensionPixelSize(R.styleable.ShapedLayout_elevation, 0)
        shadowOffset = arr.getDimension(R.styleable.ShapedLayout_elevation, 0f)

        arr.recycle()

        shadowView = ImageView(context).apply {
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        super.addView(shadowView)

        foreground = ShapedView(context).apply {
            shaper = getShaper()
            layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        super.addView(foreground)

        containerReady = true
        onContainerReady()

        setWillNotDraw(false)

    }

    abstract fun getShaper(): Shaper

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        if (containerReady) {
            foreground.addView(child, params)
        } else {
            viewsMap[child] = params
        }
    }

    private fun onContainerReady(){
        viewsMap.iterator().forEach {
            addView(it.key, it.value)
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

        if (isShadowDirty) {

            if (shadow == null) {
                shadow = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
            } else {
                shadow?.eraseColor(TRANSPARENT)
            }
            shadowPaint.apply {
                setShadowLayer(shadowRadius, shadowOffset, shadowOffset,
                        ContextCompat.getColor(context, R.color.shadow))
            }
            val c = Canvas(shadow)
            val shadowPath = getShaper().getPath(foreground.width, foreground.height).apply {
            }
            c.drawPath(shadowPath, shadowPaint)

//            val rs = RenderScript.create(context)
//            val blur = ScriptIntrinsicBlur.create(rs, Element.U8(rs))
//            val input = Allocation.createFromBitmap(rs, shadow)
//            val output = Allocation.createTyped(rs, input.type)
//            blur.setRadius(shadowRadius)
//            blur.setInput(input)
//            blur.forEach(output)
//            output.copyTo(shadow)
//            input.destroy()
//            output.destroy()
//            blur.destroy()

            shadowView.setImageBitmap(shadow)

            isShadowDirty = false
        }
    }

    companion object {
        val TAG = "ShapedView"
    }

}