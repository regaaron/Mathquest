package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Juego : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.jugar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lienzo = findViewById<CLienzo>(R.id.lienzo)

        val btn1 = findViewById<ImageButton>(R.id.btn1)
        val btn2 = findViewById<ImageButton>(R.id.btn2)
        val btn3 = findViewById<ImageButton>(R.id.btn3)
        val btn4 = findViewById<ImageButton>(R.id.btn4)

        btn1.setOnClickListener {
            lienzo.moverimg1()
        }

        btn2.setOnClickListener {
            lienzo.moverimg2()
        }

        btn3.setOnClickListener {
            lienzo.moverimg3()
        }

        btn4.setOnClickListener {
            lienzo.moverimg4()
        }

        val btnJugar = findViewById<Button>(R.id.btnJugar)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        btnJugar.setOnClickListener {
            Toast.makeText(this, "Has jugado", Toast.LENGTH_SHORT).show()
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }





    }

}