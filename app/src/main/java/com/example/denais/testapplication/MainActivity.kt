package com.example.denais.testapplication

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.denais.viewshaper.shapedlayout.ViewShaper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val ticket = findViewById<ViewShaper>(R.id.ticket)

        findViewById<Button>(R.id.bt_rotate).setOnClickListener{
//            ObjectAnimator.ofFloat(0f, 1f).apply {
//                addUpdateListener {
//                    ticket.rotationY = 360 * it.animatedFraction /2
//                    Log.d("Ticket View", "animation fraction = $animatedFraction")
//                }
////                duration = 3000
//                start()
//            }
            ticket.setShadowProperties(20f, 5f, 5f,
                    ContextCompat.getColor(baseContext, R.color.black),
                    animateChange = true)
        }
    }

}