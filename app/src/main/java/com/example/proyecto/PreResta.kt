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

class PreResta : AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pre_resta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val explanationTextView = findViewById<TextView>(R.id.tv_explanation)
        explanationTextView.text = """
           La resta es como quitar cosas para saber cuántas nos quedan.

Imagina que tienes 5 galletas 🍪🍪🍪🍪🍪. Si te comes 2 galletas 🍪🍪, ahora te quedan 3 galletas 🍪🍪🍪.

La resta es muy útil cuando necesitamos compartir cosas o saber cuántas nos quedan después de usarlas.

Ejemplo Divertido:

Si tienes 7 globos 🎈🎈🎈🎈🎈🎈🎈 y se te explotan 3 🎈🎈🎈, ¡te quedan 4 globos 🎈🎈🎈🎈 para jugar!
Así funciona la resta:

Número que tienes ➖ Número que quitas = Lo que te queda.
Mira el video para entenderlo mejor y prepararte para el juego. ¡Vamos a divertirnos! 🎉
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
            val intent = Intent(this, Desierto::class.java)
            startActivity(intent)
        }

    }

    private fun initializePlayer() {
        // Crea el reproductor ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // Configura el video desde el archivo en la carpeta raw
        val videoUri = Uri.parse("android.resource://${packageName}/raw/video_resta")
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