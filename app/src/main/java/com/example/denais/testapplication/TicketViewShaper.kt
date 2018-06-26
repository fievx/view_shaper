package com.example.denais.testapplication

import android.content.Context
import android.util.AttributeSet
import com.example.denais.viewshaper.shapedlayout.ViewShaper
import com.example.denais.viewshaper.shapedlayout.Shaper

class TicketViewShaper @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewShaper(context, attrs, defStyleAttr) {

    var ticketShaper: Shaper
    init {
        ticketShaper = TicketShaper(30f, 10f)
        onShapeReady()
    }
    override fun getShaper(): Shaper? {
        return ticketShaper
    }
}