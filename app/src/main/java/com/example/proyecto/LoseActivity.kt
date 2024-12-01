package com.example.proyecto

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoseActivity : AppCompatActivity() {
    private lateinit var gameView: GameView
    var sound:MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lose)

        gameView = findViewById(R.id.lienzo)
        gameView.knight.x=950f
        gameView.knight.y=550f
        gameView.enemy.x=-300f
        gameView.enemy.y=-550f
        gameView.win_end=true
        gameView.knight.direction="derecha"
        gameView.knight.islife=false

        var btnSalir = findViewById<Button>(R.id.btnSalir)
        btnSalir.setOnClickListener {
//            startActivity(Intent(this, Juego::class.java))
            finish()
        }
        sound = MediaPlayer.create(this, R.raw.lose)
        sound?.start()
    }


    override fun onPause() {
        super.onPause()
        gameView.pause()
    }

    override fun onResume() {
        super.onResume()
        gameView.resume()
    }
}
