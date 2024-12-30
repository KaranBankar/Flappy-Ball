package com.example.flappybird

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gameView: GameView = findViewById(R.id.gameView)
        val btnUp: ImageView = findViewById(R.id.btnUp)
        //val btnDown: Button = findViewById(R.id.btnDown)

        // Ensure buttons and GameView are correctly initialized
        btnUp.setOnClickListener {
            gameView.moveUp() // Move bird up when the "Up" button is clicked
        }

//        btnDown.setOnClickListener {
//            gameView.moveDown() // Move bird down when the "Down" button is clicked
//        }
    }
}
