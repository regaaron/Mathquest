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
    lateinit var progresoDBHelper: SQLiteHelper
    private var currentLevel: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.division)

        currentLevel = intent.getIntExtra("nivel", 1)

        gameView = findViewById(R.id.lienzo)
        gameView.enemy  = Enemy( this,300f, 550f,4)
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
        val questionTemplates = listOf(
            "Si tienes %d caramelos y los repartes entre %d niños, ¿cuántos caramelos recibe cada niño?",
            "Hay %d manzanas y quieres ponerlas en %d bolsas iguales. ¿Cuántas manzanas habrá en cada bolsa?",
            "Un pastel se corta en %d porciones y se reparte entre %d personas. ¿Cuántas porciones recibe cada persona?",
            "Un tren tiene %d pasajeros y hay %d vagones. Si todos se distribuyen equitativamente, ¿cuántos pasajeros hay en cada vagón?",
            "Si compras %d huevos y los guardas en cajas de %d, ¿cuántas cajas llenas tendrás?",
            "En una mesa hay %d sillas y las organizas en %d filas. ¿Cuántas sillas hay en cada fila?",
            "Si un edificio tiene %d habitaciones y se distribuyen en %d pisos, ¿cuántas habitaciones hay por piso?",
            "Tienes %d litros de jugo y los repartes en %d botellas. ¿Cuántos litros hay en cada botella?",
            "En una granja hay %d gallinas y se dividen en %d corrales. ¿Cuántas gallinas hay en cada corral?",
            "Un cocinero tiene %d porciones de pizza y las reparte entre %d clientes. ¿Cuántas porciones recibe cada cliente?"
        )

        // Seleccionar una plantilla aleatoria y generar el enunciado
        val questionText = questionTemplates.random().format(num1, num2)

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

    private fun checkGameOver(nivel: Int, vidas: Int) {
        println("GameOver :Ganaste")
        if (gameView.enemy.lives <= 0) {
            // El jugador ha ganado
            val puntajeActual = progresoDBHelper.obtenerPuntajeNivel(4, currentLevel)

            // Actualizar solo si las vidas obtenidas son mejores
            if (puntajeActual == null || vidas > puntajeActual) {
                progresoDBHelper.modificarNivel(4, currentLevel, vidas)
            }

            // Desbloquear el siguiente nivel si es necesario
            val siguienteNivel = currentLevel + 1
            if (siguienteNivel <= 5) { // Asegúrate de no pasar el límite de niveles
                val estadoSiguienteNivel = progresoDBHelper.obtenerPuntajeNivel(4, siguienteNivel)
                println("verifique siguiente nivel")//
                if (estadoSiguienteNivel == null) { // Si está bloqueado (NULL)
                    println("hola sou el siguiente y estoy en null")//
                    progresoDBHelper.modificarNivel(4, siguienteNivel, 0)
                // Desbloquear el nivel
                }
            }
            println("sali de verificar siguiente nivel")//

            // Avanza al siguiente nivel en la lógica del juego
            currentLevel = siguienteNivel

            val win = Intent(this, WinActivity::class.java)
            win.putExtra("vidas", gameView.knight.lives) // Enviamos el nivel como extra
            win.putExtra("niv", "division")
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
