package com.example.denais.testapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.denais.viewshaper.shapedlayout.ViewShaper

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val ticket = findViewById<ViewShaper>(R.id.ticket)

        findViewById<TextView>(R.id.bt_shadow).setOnClickListener{
            ticket.setShadowProperties(20f, 5f, 5f,
                    ContextCompat.getColor(baseContext, R.color.black),
                    animateChange = true)
        }
    }

}