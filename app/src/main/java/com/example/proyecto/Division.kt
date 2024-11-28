package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Division : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var tvOperation: TextView
    private lateinit var options: List<Button>
    private var correctAnswer: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.division1) aun no exisate el xml para la vista de las divisiones

        val nivel = intent.getIntExtra("nivel", 1)

        gameView = findViewById(R.id.lienzo)
        gameView.knight.x=500f
        gameView.enemy.x=1500f
        gameView.enemy.direction="izquierda"

        tvOperation = findViewById(R.id.tvOperation)
        options = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )

        setupNewLevel(nivel)

        options.forEach { button ->
            button.setOnClickListener {
                val userAnswer = button.text.toString().toInt()
                gameView.setUserResult(userAnswer)

                if (userAnswer == correctAnswer) {
                    gameView.knight.spriteXTarget = 1480
                    gameView.knight.moveSpriteToTarget {
                        gameView.knight.attack {
                            gameView.enemy.lives--
                            gameView.knight.spriteXTarget = 500
                            gameView.knight.moveSpriteToTarget {

                                gameView.knight.direction = "derecha" // Actualiza la dirección al final
                                checkGameOver()
                                setupNewLevel(nivel)
                            }
                        }
                    }
                } else {
                    gameView.enemy.spriteXTarget = 600
                    gameView.enemy.moveSpriteToTarget {
                        gameView.enemy.attack {
                            gameView.knight.lives--
                            gameView.enemy.spriteXTarget = 1500
                            gameView.enemy.moveSpriteToTarget {

                                gameView.enemy.direction = "izquierda" // Actualiza la dirección al final
                                checkGameOver()
                                setupNewLevel(nivel)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupNewLevel(nivel: Int) {
        val (range1, range2) = when (nivel) {
            1 -> 2..10 to 1..5 // Nivel 1: División exacta simple
            2 -> 11..20 to 1..5 // Nivel 2: Divisores pequeños, resultados enteros
            3 -> 11..20 to 2..10 // Nivel 3: División exacta con números más grandes
            4 -> 21..50 to 2..10 // Nivel 4: División de números grandes
            5 -> 51..100 to 2..20 // Nivel 5: División avanzada
            else -> 2..10 to 1..5
        }

        val num2 = range2.random()
        val correctQuotient = range1.random()
        val num1 = correctQuotient * num2 // Asegurar que sea división exacta
        correctAnswer = correctQuotient
        val questionText = "$num1 ÷ $num2 = ?"

        val level = Level(operation = questionText, expectedResult = correctAnswer)
        gameView.setLevel(level)

        val answers = mutableListOf(correctAnswer).apply {
            while (size < 4) {
                val wrongAnswer = (correctAnswer - 3..correctAnswer + 3).random()
                if (wrongAnswer != correctAnswer && !contains(wrongAnswer)) add(wrongAnswer)
            }
        }.shuffled()

        options.forEachIndexed { index, button ->
            button.text = answers[index].toString()
        }
    }
    private fun checkGameOver() {
        if (gameView.knight.lives <= 0) {
            // Ir a la pantalla de derrota
            startActivity(Intent(this, LoseActivity::class.java))
            finish()
        } else if (gameView.enemy.lives <= 0) {
            // Ir a la pantalla de victoria
            startActivity(Intent(this, WinActivity::class.java))
            finish()
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