package com.example.denais.testapplication

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.denais.testapplication.shapedlayout.ShapedLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_list)

        val ticket = findViewById<ShapedLayout>(R.id.ticket)

//        findViewById<Button>(R.id.bt_rotate).setOnClickListener{
//            ObjectAnimator.ofFloat(0f, 1f).apply {
//                addUpdateListener {
//                    ticket.rotationY = 360 * it.animatedFraction /2
//                    Log.d("Ticket View", "animation fraction = $animatedFraction")
//                }
////                duration = 3000
//                start()
//            }
//        }
    }

}