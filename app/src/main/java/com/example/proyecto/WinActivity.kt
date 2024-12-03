package com.example.proyecto

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WinActivity : AppCompatActivity() {

    private lateinit var gameView: GameView
    var sound: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_win)

        gameView = findViewById(R.id.lienzo)
        gameView.enemy  = Enemy( this,300f, 550f,1)
        gameView.knight.x=950f
        gameView.knight.y=550f
        gameView.enemy.x=-300f
        gameView.enemy.y=-550f
        gameView.win_end=true
        gameView.knight.direction="derecha"
        gameView.knight.lives = intent.getIntExtra("vidas", 1)
        var nivel = intent.getStringExtra("niv")
        var btnSalir = findViewById<Button>(R.id.btnSalir)

        btnSalir.setOnClickListener {
            Toast.makeText(this,"nivel: ${nivel}", Toast.LENGTH_SHORT).show()
            when (nivel) {
                "suma" -> startActivity(Intent(this, Montanas::class.java))
                "resta" -> startActivity(Intent(this, Desierto::class.java))
                "multiplicacion" -> startActivity(Intent(this, Volcan::class.java))
                "division" -> startActivity(Intent(this, Templo::class.java))
            }
//            finish()
        }
        sound = MediaPlayer.create(this, R.raw.win)
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

