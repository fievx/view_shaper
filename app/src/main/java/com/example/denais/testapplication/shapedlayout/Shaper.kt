package com.example.denais.testapplication.shapedlayout

import android.graphics.Path

interface Shaper {
    fun getPath(width: Int, height: Int): Path
}