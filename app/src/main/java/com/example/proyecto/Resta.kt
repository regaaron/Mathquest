package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Resta : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var tvOperation: TextView
    private lateinit var options: List<Button>
    private var correctAnswer: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resta)

        val nivel = intent.getIntExtra("nivel", 1) // Nivel actual, por defecto 1

        gameView = findViewById(R.id.lienzo)
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
                    gameView.knightAttack()
                    gameView.enemy.lives--
                    Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show()
                } else {
                    gameView.enemyAttack()
                    gameView.knight.lives--
                    Toast.makeText(this, "¡Incorrecto!", Toast.LENGTH_SHORT).show()
                }

                checkGameOver()
                setupNewLevel(nivel)
            }
        }
    }

    private fun setupNewLevel(nivel: Int) {
        val (range1, range2) = when (nivel) {
            1 -> 1..9 to 1..9 // Nivel 1: 1 dígito
            2 -> 10..99 to 1..9 // Nivel 2: 2 dígitos - 1 dígito
            3 -> 10..99 to 10..99 // Nivel 3: 2 dígitos - 2 dígitos
            4 -> 100..999 to 10..99 // Nivel 4: 3 dígitos - 2 dígitos
            5 -> 100..999 to 100..999 // Nivel 5: 3 dígitos - 3 dígitos
            else -> 1..9 to 1..9
        }

        var num1 = range1.random()
        var num2 = range2.random()
        if (num1 < num2) num1 = num2.also { num2 = num1 } // Evitar resultados negativos
        correctAnswer = num1 - num2
        val questionText = "$num1 - $num2 = ?"

        val level = Level(operation = questionText, expectedResult = correctAnswer)
        gameView.setLevel(level)

        val answers = mutableListOf(correctAnswer).apply {
            while (size < 4) {
                val wrongAnswer = (correctAnswer - 10..correctAnswer + 10).random()
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
