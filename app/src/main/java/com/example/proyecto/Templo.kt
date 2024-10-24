package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Templo : AppCompatActivity(){

    lateinit var lienzo: CLienzo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.templo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lienzo = findViewById(R.id.lienzo)
        lienzo.spriteX=200f
        lienzo.spriteY=280f


        lienzo = findViewById(R.id.lienzo)
        lienzo.spriteX=200f
        lienzo.spriteY=280f

        var btn1 = findViewById<ImageButton>(R.id.btn1)
        var btn2 = findViewById<ImageButton>(R.id.btn2)
        var btn3 = findViewById<ImageButton>(R.id.btn3)
        var btn4 = findViewById<ImageButton>(R.id.btn4)
        var btn5 = findViewById<ImageButton>(R.id.btn5)
        var btn6 = findViewById<ImageButton>(R.id.btn6)

        var btnSalir = findViewById<Button>(R.id.btnSalir)
        var btnJugar = findViewById<Button>(R.id.btnJugar)

        btn1.setOnClickListener {
            lienzo.spriteXTarget=100f
            lienzo.moveSpriteToTarget()
        }

        btn2.setOnClickListener {
            lienzo.spriteXTarget=480f
            lienzo.moveSpriteToTarget()
        }

        btn3.setOnClickListener {
            lienzo.spriteXTarget=840f
            lienzo.moveSpriteToTarget()
        }

        btn4.setOnClickListener {
            lienzo.spriteXTarget=1180f
            lienzo.moveSpriteToTarget()
        }

        btn5.setOnClickListener {
            lienzo.spriteXTarget=1560f
            lienzo.moveSpriteToTarget()
        }

        btn6.setOnClickListener {
            lienzo.spriteXTarget=1920f
            lienzo.moveSpriteToTarget()
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, Juego::class.java)
            startActivity(intent)
        }
    }

}