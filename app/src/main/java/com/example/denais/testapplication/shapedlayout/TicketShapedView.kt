package com.example.denais.testapplication.shapedlayout

import android.content.Context
import android.graphics.*
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.widget.FrameLayout
import com.example.denais.testapplication.R

class TicketShapedView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout (context, attrs, defStyleAttr) {

    var shapePath: Path

    init {
        shapePath = getPath()
        setWillNotDraw(false)
    }

    override fun draw(canvas: Canvas) {
        canvas.clipPath(shapePath)
        super.draw(canvas)
    }

    private fun getPath(): Path {
        val radius = context.resources.getDimensionPixelSize(R.dimen.corner_radius)
        val elevation = 50f
        return TicketShaper.getShape(height, width, radius.toFloat(), elevation)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        shapePath = getPath()
    }
}