package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        var btnJugar = findViewById<ImageButton>(R.id.btnJugar)
        btnJugar.setOnClickListener {
            val intent = Intent(this, Juego::class.java)
            startActivity(intent)
        }

        var btnAyuda = findViewById<ImageButton>(R.id.btnAyuda)
        btnAyuda.setOnClickListener {
            val intent = Intent(this, Ayuda::class.java)
            startActivity(intent)
        }

        var btnSalir = findViewById<ImageButton>(R.id.btnSalir)
        btnSalir.setOnClickListener{
            Toast.makeText(this, "Gracias por jugar", Toast.LENGTH_SHORT).show()
        }

    }
}