package com.example.denais.testapplication

import android.content.Context
import android.util.AttributeSet
import com.example.denais.viewshaper.shapedlayout.ShapedLayout
import com.example.denais.viewshaper.shapedlayout.Shaper

class TicketShapedLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ShapedLayout(context, attrs, defStyleAttr) {

    override fun getShaper(): Shaper {
        return TicketShaper(30f, 3f)
    }
}