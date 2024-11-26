package com.example.proyecto

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

class PreDivision : AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pre_division)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val explanationTextView = findViewById<TextView>(R.id.tv_explanation)
        explanationTextView.text = """
La división es como repartir cosas en partes iguales.

Imagina que tienes 12 caramelos 🍬🍬🍬🍬🍬🍬🍬🍬🍬🍬🍬🍬 y quieres compartirlos entre 4 amigos.
¿Cuántos caramelos recibe cada amigo?
12 caramelos ÷ 4 amigos = 3 caramelos por amigo. 🎉

Es muy útil cuando queremos compartir o dividir algo en partes iguales.

Ejemplo Divertido:

Si tienes 10 globos 🎈🎈🎈🎈🎈🎈🎈🎈🎈🎈 y los repartes entre 2 amigos, ¿cuántos globos le tocan a cada uno?
10 ÷ 2 = 5 globos por amigo. 🎈🎈🎈🎈🎈
Así funciona la división:

Número total ÷ Número de partes = Lo que le toca a cada uno.
Mira el video para aprender cómo dividir de manera fácil y divertida. ¡Te encantará! 🎊
        """.trimIndent()

        // Inicializa el PlayerView
        playerView = findViewById(R.id.player_view)
        initializePlayer()

        // Configura los botones
        val btnBack = findViewById<Button>(R.id.btn_back)
        val btnContinue = findViewById<Button>(R.id.btn_continue)

        btnBack.setOnClickListener {
            // Regresar a la actividad anterior
            finish()
        }

        btnContinue.setOnClickListener {
            // Detiene el reproductor antes de cambiar de actividad
            releasePlayer()
            val intent = Intent(this, Templo::class.java)
            startActivity(intent)
        }

    }

    private fun initializePlayer() {
        // Crea el reproductor ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // Configura el video desde el archivo en la carpeta raw
        val videoUri = Uri.parse("android.resource://${packageName}/raw/video_division")
        val mediaItem = MediaItem.fromUri(videoUri)

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera el reproductor al salir de la actividad
        exoPlayer.release()
    }

    private fun releasePlayer() {
        if (::exoPlayer.isInitialized) {
            exoPlayer.stop() // Detiene la reproducción
            exoPlayer.release() // Libera los recursos
        }
    }
}