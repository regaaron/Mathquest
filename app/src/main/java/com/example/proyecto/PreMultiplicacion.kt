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

class PreMultiplicacion: AppCompatActivity() {
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var playerView: PlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pre_multiplicacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        val explanationTextView = findViewById<TextView>(R.id.tv_explanation)
        explanationTextView.text = """
La multiplicación es como hacer sumas rápidas.

Imagina que tienes 3 bolsitas 🎒🎒🎒 y en cada bolsita hay 4 caramelos 🍬🍬🍬🍬.
En lugar de sumar 4 + 4 + 4, podemos usar la multiplicación:
3 bolsitas × 4 caramelos = 12 caramelos. 🎉

¡Es mucho más rápido que sumar uno por uno!

Ejemplo Divertido:

Si cada día comes 2 helados 🍦🍦 y lo haces durante 5 días, ¿cuántos helados comerás en total?
2 × 5 = 10 helados. 🍦🍦🍦🍦🍦🍦🍦🍦🍦🍦
Así funciona la multiplicación:

Número de grupos ✖️ Cantidad en cada grupo = Total.
Es como tener muchos grupos iguales y contarlos rápido.

Mira el video para aprender más y prepárate para multiplicar como un campeón. ¡Es divertido y súper útil! 🎊
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
            val intent = Intent(this, Volcan::class.java)
            startActivity(intent)
        }

    }

    private fun initializePlayer() {
        // Crea el reproductor ExoPlayer
        exoPlayer = ExoPlayer.Builder(this).build()
        playerView.player = exoPlayer

        // Configura el video desde el archivo en la carpeta raw
        val videoUri = Uri.parse("android.resource://${packageName}/raw/video_multiplicacion")
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