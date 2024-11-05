package com.example.proyecto

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Suma1 : AppCompatActivity() {

    private lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.suma1)

        gameView = findViewById(R.id.lienzo)
        gameView.resume()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        findViewById<Button>(R.id.btnMoveLeft1).setOnClickListener {
            gameView.moveKnightLeft()
        }

        findViewById<Button>(R.id.btnMoveRight1).setOnClickListener {
            gameView.moveKnightRight()
        }

        findViewById<Button>(R.id.btnStop1).setOnClickListener {
//            if(gameView.isPlaying){
//                gameView.pause()
//            }else{
//                gameView.resume()
//            }
//            gameView.knight.spriteXTarget=500
//            gameView.knight.moveSpriteToTarget()
//            gameView.knight.lives--
            gameView.knight.stopMoving()
        }

        findViewById<Button>(R.id.btnRestarVida).setOnClickListener {
            gameView.enemy.lives--
        }

        findViewById<Button>(R.id.btnTarget).setOnClickListener {
            gameView.knight.spriteXTarget=500
            gameView.knight.moveSpriteToTarget()
        }

        findViewById<Button>(R.id.btnPausar).setOnClickListener{
            if (gameView.isPlaying){
                gameView.pause()
            }else{
                gameView.resume()
            }
        }

        findViewById<Button>(R.id.btnAttack1).setOnClickListener {
            gameView.knightAttack()
        }

        findViewById<Button>(R.id.btnMoveLeft2).setOnClickListener {
            gameView.moveEnemyLeft()
        }

        findViewById<Button>(R.id.btnMoveRight2).setOnClickListener {
            gameView.moveEnemyRight()
        }

        findViewById<Button>(R.id.btnStop2).setOnClickListener {
            gameView.stopEnemy()
        }

        findViewById<Button>(R.id.btnAttack2).setOnClickListener {
            gameView.enemyAttack()
        }
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
