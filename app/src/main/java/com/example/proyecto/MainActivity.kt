package com.example.proyecto

import android.content.Intent
import android.media.MediaPlayer
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

        // Usar MusicManager para manejar la música
        MusicManager.startMusic(this, R.raw.fondo)

        var btnJugar = findViewById<ImageButton>(R.id.btnJugar)
        btnJugar.setOnClickListener {
            val intent = Intent(this, Juego::class.java)
            startActivity(intent)
        }

        var btnAyuda = findViewById<ImageButton>(R.id.btnAyuda)
        btnAyuda.setOnClickListener {
            val intent = Intent(this, MenuMiniJuegos::class.java)
            startActivity(intent)
        }

        var btnSalir = findViewById<ImageButton>(R.id.btnSalir)
        btnSalir.setOnClickListener {
            Toast.makeText(this, "Gracias por jugar", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicManager.stopMusic()
    }
}