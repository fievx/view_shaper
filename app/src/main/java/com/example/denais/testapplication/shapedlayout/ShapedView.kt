package com.example.denais.testapplication.shapedlayout

import android.content.Context
import android.graphics.*
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet

class ShapedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout (context, attrs, defStyleAttr) {

    lateinit var shapePath: Path
    lateinit var shaper: Shaper

    init {
        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {
        canvas.clipPath(shapePath)
        super.draw(canvas)
    }

    private fun getPath(): Path {
        return shaper.getPath(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shapePath = getPath()
    }
}