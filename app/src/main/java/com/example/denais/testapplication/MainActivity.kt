package com.example.denais.testapplication

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
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