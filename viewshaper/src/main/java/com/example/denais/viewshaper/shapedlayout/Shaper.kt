package com.example.denais.viewshaper.shapedlayout

import android.graphics.Path

interface Shaper {
    fun getPath(width: Int, height: Int): Path
}