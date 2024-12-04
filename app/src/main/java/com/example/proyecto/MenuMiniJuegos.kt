package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MenuMiniJuegos  : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.menu_mini_juegos)

        var btnMemorama = findViewById<ImageButton>(R.id.btnMemorama)
        btnMemorama.setOnClickListener {
            val intent = Intent(this, Memorama::class.java)
            startActivity(intent)
        }

        var btnModoInifinito = findViewById<ImageButton>(R.id.btnModoInfinito)
        btnModoInifinito.setOnClickListener {
            val intent = Intent(this, Infinito::class.java)
            startActivity(intent)
        }

        var btnRegresar = findViewById<ImageButton>(R.id.btnRegresar)
        btnRegresar.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}