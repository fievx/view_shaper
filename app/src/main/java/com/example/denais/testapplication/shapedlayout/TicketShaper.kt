package com.example.denais.testapplication.shapedlayout

import android.graphics.Path
import android.graphics.RectF

object TicketShaper {

    fun getShape(height: Int, width: Int, cornerRadius: Float, elevation: Float): Path {
        return Path().apply {
            val cutoutWidth = width / 8f
            val cutoutHeight = cutoutWidth

            val padding = elevation
            val pathLeft = padding
            val pathRight = width - padding
            val pathTop = padding
            val pathBottom = height.toFloat() - padding

            val cutoutXStart = width / 2 - cutoutWidth
            val cutoutXEnd = cutoutXStart + cutoutWidth * 2
            val topCutoutYStart = pathTop - cutoutHeight
            val topCutoutYEnd = pathTop + cutoutHeight

            val botCutoutYStart = pathBottom - cutoutHeight
            val botCutoutYEnd = pathBottom + cutoutHeight

            fillType = Path.FillType.WINDING
            moveTo(pathLeft + cornerRadius, pathTop)
            lineTo(cutoutXStart, pathTop)

            // top cutout
            arcTo(RectF(cutoutXStart, topCutoutYStart, cutoutXEnd, topCutoutYEnd), 180f, -180f)
            lineTo(pathRight - cornerRadius, pathTop)

            //top right corner
            arcTo(RectF(pathRight - cornerRadius *2 , pathTop, pathRight, pathTop + cornerRadius *2), 270f, 90f)
            lineTo(pathRight, pathBottom - cornerRadius)

            //bottom right corner
            arcTo(RectF(pathRight - cornerRadius * 2, pathBottom - cornerRadius * 2, pathRight, pathBottom), 0f, 90f)
            lineTo(cutoutXEnd, pathBottom)

            // bottom cutour
            arcTo(RectF(cutoutXStart, botCutoutYStart, cutoutXEnd, botCutoutYEnd), 0f, -180f)
            lineTo(pathLeft + cornerRadius, pathBottom)

            //bottom left corner
            arcTo(RectF(pathLeft, pathBottom - cornerRadius * 2, pathLeft + cornerRadius * 2, pathBottom), 90f, 90f)
            lineTo(pathLeft, pathTop - cornerRadius)

            //top left corner
            arcTo(RectF(pathLeft, pathTop, pathLeft + cornerRadius * 2, pathTop + cornerRadius * 2), 180f, 90f)
            close()
        }
    }
}