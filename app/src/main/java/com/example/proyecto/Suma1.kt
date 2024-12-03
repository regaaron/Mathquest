package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Suma1 : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var tvOperation: TextView
    private lateinit var options: List<Button>
    private var correctAnswer: Int = 0
    lateinit var progresoDBHelper: SQLiteHelper
    private var currentLevel: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suma1)

        currentLevel = intent.getIntExtra("nivel", 1)

        gameView = findViewById(R.id.lienzo)
        gameView.enemy  = Enemy( this,300f, 550f,1)
        gameView.knight.x=500f
        gameView.enemy.x=1500f
        gameView.enemy.direction="izquierda"

        progresoDBHelper = SQLiteHelper(this) //base de datos

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
        // Rango de números según el nivel
        val (range1, range2) = when (nivel) {
            1 -> 1..9 to 1..9 // Nivel 1: 1 dígito
            2 -> 10..99 to 1..9 // Nivel 2: 2 dígitos + 1 dígito
            3 -> 10..99 to 10..99 // Nivel 3: 2 dígitos + 2 dígitos
            4 -> 100..999 to 10..99 // Nivel 4: 3 dígitos + 2 dígitos
            5 -> 100..999 to 100..999 // Nivel 5: 3 dígitos + 3 dígitos
            else -> 1..9 to 1..9 // Default a nivel 1
        }

        // Generar números aleatorios
        val num1 = range1.random()
        val num2 = range2.random()
        correctAnswer = num1 + num2

        // Banco de enunciados
        val questionTemplates = listOf(
            "Si Juan tiene %d manzanas y le dan %d más, ¿cuántas tiene?",
            "En una bolsa hay %d caramelos. Si le añades %d más, ¿cuántos hay en total?",
            "Un árbol tiene %d hojas y crecen %d más. ¿Cuántas hojas tiene ahora?",
            "En una caja hay %d juguetes. Si le añades %d más, ¿cuántos juguetes hay en total?",
            "Hay %d niños en el parque y llegan %d más. ¿Cuántos niños hay ahora?",
            "Si un avión lleva %d pasajeros y suben %d más, ¿cuántos pasajeros hay en total?",
            "Tienes %d lápices en tu estuche y compras %d más. ¿Cuántos tienes ahora?",
            "Un barco transporta %d cajas. Si le cargan %d más, ¿cuántas cajas lleva en total?",
            "En un estadio hay %d espectadores. Si llegan %d más, ¿cuántos espectadores hay ahora?",
            "Una vaca produce %d litros de leche y otra vaca produce %d más. ¿Cuántos litros producen entre las dos?"
        )

        // Seleccionar una plantilla aleatoria y generar el enunciado
        val questionText = questionTemplates.random().format(num1, num2)

        // Configurar el nivel en GameView
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



//    private fun checkGameOver() {
//        if (gameView.knight.lives <= 0) {
//            // Ir a la pantalla de derrota
//            startActivity(Intent(this, LoseActivity::class.java))
//            finish()
//        } else if (gameView.enemy.lives <= 0) {
//            // Ir a la pantalla de victoria
//            startActivity(Intent(this, WinActivity::class.java))
//            finish()
//        }
//    }

    private fun checkGameOver(nivel: Int, vidas: Int) {
        println("GameOver :Ganaste")
        if (gameView.enemy.lives <= 0) {
            // El jugador ha ganado
            val puntajeActual = progresoDBHelper.obtenerPuntajeNivel(1, currentLevel)

            // Actualizar solo si las vidas obtenidas son mejores
            if (vidas > puntajeActual!!) {
                progresoDBHelper.modificarNivel(1, currentLevel, vidas)
            }

            // Desbloquear el siguiente nivel si es necesario
            val siguienteNivel = currentLevel + 1
            if (siguienteNivel <= 5) { // Asegúrate de no pasar el límite de niveles
                val estadoSiguienteNivel = progresoDBHelper.obtenerPuntajeNivel(1, siguienteNivel)
                if (estadoSiguienteNivel == null) { // Si está bloqueado (NULL)
                    progresoDBHelper.modificarNivel(1, siguienteNivel, 0) // Desbloquear el nivel
                }
            }

            // Avanza al siguiente nivel en la lógica del juego
            currentLevel = siguienteNivel

            val win = Intent(this, WinActivity::class.java)
            win.putExtra("vidas", gameView.knight.lives) // Enviamos el nivel como extra
            win.putExtra("niv", "suma")
            startActivity(win)
            finish()
        }
        if (gameView.knight.lives <= 0) {
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
