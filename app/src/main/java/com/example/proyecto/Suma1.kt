package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Suma1 : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var tvOperation: TextView
    private lateinit var options: List<Button>
    private var correctAnswer: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suma1)

        gameView = findViewById(R.id.lienzo)
        tvOperation = findViewById(R.id.tvOperation)
        options = listOf(
            findViewById(R.id.btnOption1),
            findViewById(R.id.btnOption2),
            findViewById(R.id.btnOption3),
            findViewById(R.id.btnOption4)
        )

        setupNewLevel()

        options.forEach { button ->
            button.setOnClickListener {
                val userAnswer = button.text.toString().toInt()
                gameView.setUserResult(userAnswer) // Enviamos el resultado al GameView

                // Actualizamos la lógica del ataque
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
                setupNewLevel()
            }
        }
    }

    private fun setupNewLevel() {
        // Generar nueva pregunta para el nivel de suma
        val num1 = (1..10).random()
        val num2 = (1..10).random()
        correctAnswer = num1 + num2
        val questionText = "$num1 + $num2 = ?"

        // Configuramos el nivel en GameView
        val level = Level(operation = questionText, expectedResult = correctAnswer)
        gameView.setLevel(level)

        // Generar respuestas para las opciones
        val answers = mutableListOf(correctAnswer).apply {
            while (size < 4) {
                val wrongAnswer = (correctAnswer - 10..correctAnswer + 10).random()
                if (wrongAnswer != correctAnswer && !contains(wrongAnswer)) add(wrongAnswer)
            }
        }.shuffled()

        // Asignar las respuestas a los botones
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
