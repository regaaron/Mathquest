package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Infinito : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var tvOperation: TextView
    private lateinit var options: List<Button>
    private var correctAnswer: Int = 0
    lateinit var progresoDBHelper: SQLiteHelper
    private var currentLevel: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.division)

        gameView = findViewById(R.id.lienzo)
        gameView.enemy  = Enemy( this,300f, 550f,1)
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

        setupNewLevel()

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
                                setupNewLevel()
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
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupNewLevel() {
        // Seleccionar operación aleatoria: 1 = Suma, 2 = Resta, 3 = Multiplicación, 4 = División
        val tipoOperacion = (1..4).random()

        gameView.enemy  = Enemy( this,300f, 550f,tipoOperacion)
        gameView.enemy.x=1500f
        gameView.enemy.direction="izquierda"

        // Seleccionar nivel de dificultad aleatorio: 1 a 5
        val nivel = (1..5).random()

        // Configuración de rangos según el nivel
        val (range1, range2) = when (nivel) {
            1 -> 1..5 to 1..5
            2 -> 6..10 to 1..5
            3 -> 6..10 to 6..10
            4 -> 11..20 to 1..10
            5 -> 11..20 to 11..20
            else -> 1..5 to 1..5
        }

        val num1 = range1.random()
        val num2 = range2.random()
        val (questionText, result) = when (tipoOperacion) {
            1 -> {
                val result = num1 + num2
                Pair("Si sumas $num1 y $num2, ¿cuál es el resultado?", result)
            }
            2 -> {
                val larger = maxOf(num1, num2)
                val smaller = minOf(num1, num2)
                val result = larger - smaller
                Pair("Si restas $smaller a $larger, ¿cuál es el resultado?", result)
            }
            3 -> {
                val result = num1 * num2
                Pair(
                    "En una mesa hay $num1 filas de $num2 sillas. ¿Cuántas sillas hay en total?",
                    result
                )
            }
            4 -> {
                val dividend = num1 * num2
                val result = num1
                Pair("Si divides $dividend entre $num2, ¿cuál es el resultado?", result)
            }
            else -> Pair("Error", 0)
        }

        // Asignar el resultado correcto
        correctAnswer = result

        // Crear el nivel con la operación y el resultado esperado
        val level = Level(operation = questionText, expectedResult = correctAnswer)
        gameView.setLevel(level)

        // Generar respuestas aleatorias
        val answers = mutableListOf(correctAnswer).apply {
            while (size < 4) {
                val wrongAnswer = (correctAnswer - 10..correctAnswer + 10).random()
                if (wrongAnswer != correctAnswer && !contains(wrongAnswer)) add(wrongAnswer)
            }
        }.shuffled()

        println("Correct Answer: $correctAnswer, Options: $answers") // Debugging

        // Asignar respuestas a los botones
        options.forEachIndexed { index, button ->
            button.text = answers[index].toString()
        }
    }


    private fun checkGameOver() {
        println("GameOver :Ganaste")
        if(gameView.knight.lives <= 0){
            val lose = Intent(this, LoseActivity::class.java)
            startActivity(lose)
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