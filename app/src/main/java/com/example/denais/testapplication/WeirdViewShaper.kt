package com.example.denais.testapplication

import android.content.Context
import android.graphics.Path
import android.util.AttributeSet
import com.example.denais.viewshaper.shapedlayout.Shaper
import com.example.denais.viewshaper.shapedlayout.ViewShaper

class WeirdViewShaper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewShaper(context, attrs, defStyleAttr) {

    override fun getShaper(): Shaper? {
        return object : Shaper{
            override fun getPath(width: Int, height: Int): Path {
                val centerX = width/2f
                val centerYTop = height/3f
                val centerYBottom = height - height/3f
                val right = width.toFloat()
                val bottom = height.toFloat()
                return Path().apply {
                    moveTo(0f, 0f)
                    lineTo(centerX, centerYTop)
                    lineTo(right, 0f)
                    lineTo(right, bottom)
                    lineTo(centerX, centerYBottom)
                    lineTo(0f, bottom)
                    close()
                }
            }
        }
    }
}