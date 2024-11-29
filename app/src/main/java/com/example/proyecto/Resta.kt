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
    lateinit var progresoDBHelper: SQLiteHelper
    private var currentLevel: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resta)

        currentLevel = intent.getIntExtra("nivel", 1)
        progresoDBHelper = SQLiteHelper(this) //base de datos

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

        setupNewLevel(currentLevel)

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
                                checkGameOver(1,gameView.knight.lives)
                                setupNewLevel(currentLevel)
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
                                checkGameOver(1,gameView.knight.lives)

                                setupNewLevel(currentLevel)
                            }
                        }
                    }
                }
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


    private fun checkGameOver(nivel: Int, vidas: Int) {
        println("GameOver :Ganaste")
        if (gameView.enemy.lives <= 0) {
            // El jugador ha ganado
            val puntajeActual = progresoDBHelper.obtenerPuntajeNivel(2, currentLevel)

            // Actualizar solo si las vidas obtenidas son mejores
            if (vidas > puntajeActual!!) {
                progresoDBHelper.modificarNivel(2, currentLevel, vidas)
            }

            // Desbloquear el siguiente nivel si es necesario
            val siguienteNivel = currentLevel + 1
            if (siguienteNivel <= 5) { // Asegúrate de no pasar el límite de niveles
                val estadoSiguienteNivel = progresoDBHelper.obtenerPuntajeNivel(2, siguienteNivel)
                if (estadoSiguienteNivel == null) { // Si está bloqueado (NULL)
                    progresoDBHelper.modificarNivel(2, siguienteNivel, 0) // Desbloquear el nivel
                }
            }

            // Avanza al siguiente nivel en la lógica del juego
            currentLevel = siguienteNivel

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
