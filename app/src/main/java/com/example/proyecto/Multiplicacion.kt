package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Multiplicacion : AppCompatActivity() {

    private lateinit var gameView: GameView
    private lateinit var tvOperation: TextView
    private lateinit var options: List<Button>
    private var correctAnswer: Int = 0
    lateinit var progresoDBHelper: SQLiteHelper
    private var currentLevel: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.multiplicacion)

        currentLevel = intent.getIntExtra("nivel", 1)

        gameView = findViewById(R.id.lienzo)
        gameView.enemy  = Enemy( this,300f, 550f,3)
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
        val (range1, range2) = when (nivel) {
            1 -> 1..5 to 1..5 // Nivel 1: Tablas del 1 al 5
            2 -> 6..10 to 1..5 // Nivel 2: Tablas más grandes con multiplicadores pequeños
            3 -> 6..10 to 6..10 // Nivel 3: Tablas completas
            4 -> 11..20 to 1..10 // Nivel 4: Multiplicadores más grandes
            5 -> 11..20 to 11..20 // Nivel 5: Multiplicación completa de números grandes
            else -> 1..5 to 1..5
        }

        val num1 = range1.random()
        val num2 = range2.random()
        correctAnswer = num1 * num2
        val questionTemplates = listOf(
            "Si tienes %d bolsas con %d manzanas cada una, ¿cuántas manzanas tienes en total?",
            "En una mesa hay %d filas de %d sillas. ¿Cuántas sillas hay en total?",
            "Si compras %d cajas de huevos y cada una tiene %d huevos, ¿cuántos huevos tienes?",
            "Un tren tiene %d vagones y cada vagón lleva %d pasajeros. ¿Cuántos pasajeros hay en total?",
            "Si plantas %d árboles y cada uno da %d frutos, ¿cuántos frutos tendrás?",
            "Un edificio tiene %d pisos y cada piso tiene %d habitaciones. ¿Cuántas habitaciones hay en total?",
            "En una granja hay %d corrales y cada corral tiene %d gallinas. ¿Cuántas gallinas hay en total?",
            "Si haces %d filas de %d sillas, ¿cuántas sillas tienes en total?",
            "Si cada alumno tiene %d lápices y hay %d alumnos en la clase, ¿cuántos lápices hay en total?",
            "Un pastelero hace %d pasteles y cada pastel tiene %d pisos. ¿Cuántos pisos de pastel hay en total?"
        )

        // Seleccionar una plantilla aleatoria y generar el enunciado
        val questionText = questionTemplates.random().format(num1, num2)

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
            val puntajeActual = progresoDBHelper.obtenerPuntajeNivel(3, currentLevel)

            // Actualizar solo si las vidas obtenidas son mejores
            if (puntajeActual == null || vidas > puntajeActual) {
                progresoDBHelper.modificarNivel(3, currentLevel, vidas)
            }

            // Desbloquear el siguiente nivel si es necesario
            val siguienteNivel = currentLevel + 1
            if (siguienteNivel <= 5) { // Asegúrate de no pasar el límite de niveles
                val estadoSiguienteNivel = progresoDBHelper.obtenerPuntajeNivel(3, siguienteNivel)
                if (estadoSiguienteNivel == null) { // Si está bloqueado (NULL)
                    progresoDBHelper.modificarNivel(3, siguienteNivel, 0) // Desbloquear el nivel
                }
            }

            // Avanza al siguiente nivel en la lógica del juego
            currentLevel = siguienteNivel

            val win = Intent(this, WinActivity::class.java)
            win.putExtra("vidas", gameView.knight.lives)
            win.putExtra("niv", "multiplicacion")
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