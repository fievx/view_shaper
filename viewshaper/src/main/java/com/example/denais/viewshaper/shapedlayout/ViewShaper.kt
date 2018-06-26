package com.example.denais.viewshaper.shapedlayout

import android.animation.ObjectAnimator
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
abstract class ViewShaper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout (context, attrs, defStyleAttr) {

    private var shadow: Bitmap? = null
    private val shadowPaint = Paint(ANTI_ALIAS_FLAG)

    private var shadowRadius = context.resources.getDimension(R.dimen.shadow_default_radius)
    private var shadowXOffset = context.resources.getDimension(R.dimen.shadow_default_x_translation)
    private var shadowYOffset = context.resources.getDimension(R.dimen.shadow_default_y_translation)
    private var shadowColor = ContextCompat.getColor(context, R.color.shadow_default_color)
    var hasShadow = false
    private var foregroundPadding = 0

    private lateinit var foreground: ShapedView

    private lateinit var shadowView: ImageView
    var isShadowDirty = true

    var containerReady = false

    val viewsMap = mutableMapOf<View?, ViewGroup.LayoutParams?>()

    init {
        val arr = context.obtainStyledAttributes(attrs, R.styleable.ViewShaper)
        shadowXOffset = arr.getDimension(R.styleable.ViewShaper_shadow_offset, shadowXOffset)
        shadowYOffset = arr.getDimension(R.styleable.ViewShaper_shadow_offset, shadowYOffset)

        foregroundPadding = arr.getDimensionPixelSize(R.styleable.ViewShaper_padding, 0)

        if (arr.hasValue(R.styleable.ViewShaper_shadow_x_offset)){
            shadowXOffset = arr.getDimension(R.styleable.ViewShaper_shadow_x_offset, shadowXOffset)
        }

        if (arr.hasValue(R.styleable.ViewShaper_shadow_y_offset)){
            shadowYOffset = arr.getDimension(R.styleable.ViewShaper_shadow_y_offset, shadowYOffset)

        }

        shadowRadius = arr.getDimension(R.styleable.ViewShaper_shadow_radius, shadowRadius)

        hasShadow = arr.getBoolean(R.styleable.ViewShaper_has_shadow, true)

        arr.recycle()

        if(getShaper() != null){
            onShapeReady()
        }

        setWillNotDraw(false)

    }

    fun setShadowProperties(radius: Float = shadowRadius,
                            xOffset: Float = shadowXOffset,
                            yOffset: Float = shadowYOffset,
                            color: Int = shadowColor,
                            animateChange: Boolean = false){
        if (animateChange.not()) {
            shadowRadius = radius
            shadowXOffset = xOffset
            shadowYOffset = yOffset
            shadowColor = color
            isShadowDirty = true
            generateShadow()
        } else {
            val oldRadius = shadowRadius
            val oldXOffset = shadowXOffset
            val oldYOffset = shadowYOffset
            val oldColor = shadowColor
            ObjectAnimator.ofFloat(0f, 1f).apply {
                addUpdateListener {
                    shadowRadius = oldRadius + (radius - oldRadius) * it.animatedFraction
                    shadowXOffset = oldXOffset + (xOffset - oldXOffset) * it.animatedFraction
                    shadowYOffset = oldYOffset + (yOffset - oldYOffset) * it.animatedFraction
                    shadowColor = (oldColor + (color - oldColor) * it.animatedFraction).toInt()
                    isShadowDirty = true
                    generateShadow()
                }
                start()
            }
        }
    }

    fun onShapeReady(){
        if (containerReady.not()) {
            shadowView = ImageView(context).apply {
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
            super.addView(shadowView)

            foreground = ShapedView(context).apply {
                shaper = getShaper()
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                    setMargins(foregroundPadding, foregroundPadding, foregroundPadding, foregroundPadding)
                }
            }
            super.addView(foreground)

            containerReady = true
            onContainerReady()
        }
    }

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
        if (isShadowDirty && hasShadow) {

            if (shadow == null) {
                shadow = Bitmap.createBitmap(shadowView.width, shadowView.height, Bitmap.Config.ALPHA_8)
            } else {
                shadow?.eraseColor(TRANSPARENT)
            }
            shadowPaint.apply {
                colorFilter = PorterDuffColorFilter(shadowColor, PorterDuff.Mode.SRC_IN)
                setShadowLayer(shadowRadius, shadowXOffset, shadowYOffset, shadowColor)
            }
            val c = Canvas(shadow)
            getShaper()?.getPath(foreground.width, foreground.height)?.let { path->
                path.offset(foregroundPadding.toFloat(), foregroundPadding.toFloat())
                c.drawPath(path, shadowPaint)
                shadowView.setImageBitmap(shadow)
                isShadowDirty = false
            }
        }
    }

    abstract fun getShaper(): Shaper?

    companion object {
        val TAG = "ShapedView"
    }

}
