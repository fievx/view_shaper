package com.example.denais.viewshaper.shapedlayout

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.FrameLayout

internal class ShapedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout (context, attrs, defStyleAttr) {

    var shapePath: Path? = null
    var shaper: Shaper? = null

    init {
        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {
        if (shapePath != null) {
            canvas.clipPath(shapePath)
        }
        super.draw(canvas)
    }

    private fun getPath(): Path? {
        return shaper?.getPath(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shapePath = getPath()
    }
}