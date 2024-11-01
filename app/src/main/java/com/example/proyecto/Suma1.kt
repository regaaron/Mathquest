package com.example.proyecto

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Suma1 : AppCompatActivity(){

    lateinit var lienzo: CLienzo



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.suma1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels
        lienzo = findViewById(R.id.lienzo)
        lienzo.spriteWidth = 400f
        lienzo.spriteHeight = 400f
        lienzo.spriteX = 300f
        lienzo.spriteY = 500f

        var btnJugar = findViewById<Button>(R.id.btnJugar)

        btnJugar.setOnClickListener {
//            lienzo.attack()
        }


    }

}