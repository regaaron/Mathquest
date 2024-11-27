package com.example.proyecto

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceView

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : SurfaceView(context, attrs), Runnable {

    var isPlaying = false
    private var thread: Thread? = null
    val enemy = Enemy(context, 300f, 550f)
    val knight = Knight(context, 100f, 700f)

    private val paint = Paint()
    private var elapsedTime = 0L // Tiempo transcurrido en milisegundos
    private var lastUpdateTime = System.currentTimeMillis() // Marca de tiempo de la última actualización

    private var currentLevel: Level? = null // Nivel actual
    private var userResult: Int? = null // Resultado ingresado por el usuario
    private var gameState: String? = null // "Ganaste" o "Perdiste"
    var win_end = false;
    companion object {
        const val SPRITE_WIDTH = 300 // Define el tamaño deseado
        const val SPRITE_HEIGHT = 300
        const val SPRITE_WIDTH2 = 600 // Define el tamaño deseado
        const val SPRITE_HEIGHT2 = 600
    }

    init {
        holder.setFormat(android.graphics.PixelFormat.TRANSPARENT)
        setZOrderOnTop(true)
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            Thread.sleep(32)
        }
    }

    fun update() {
        // Actualiza el tiempo transcurrido
        val currentTime = System.currentTimeMillis()
        elapsedTime += currentTime - lastUpdateTime
        lastUpdateTime = currentTime

        // Lógica del juego
        knight.update()
        enemy.update()

        // Verifica si hay un resultado ingresado
        if (userResult != null && gameState == null) {
            gameState = if (userResult == currentLevel?.expectedResult) "Ganaste" else "Perdiste"
        }
    }

    fun draw() {
        if (holder.surface.isValid) {
            val canvas = holder.lockCanvas()

            // Dibuja el fondo transparente
            canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)

            knight.draw(canvas)
            enemy.draw(canvas)

            // Dibuja el recuadro para el enunciado
            paint.color = Color.WHITE
            paint.style = Paint.Style.FILL
            val rect = Rect(50, 50, width - 50, 200)
            if (!win_end){
                canvas.drawRect(rect, paint)
            }


            // Dibuja el texto del enunciado
            paint.color = Color.BLACK
            paint.textSize = 50f
            paint.textAlign = Paint.Align.CENTER
            val operation = currentLevel?.operation ?: "Esperando nivel"
            if(!win_end){

                canvas.drawText(operation, width / 2f, 150f, paint)
            }


            // Dibuja el estado del juego si aplica
            if (gameState != null) {
                paint.color = if (gameState == "Ganaste") Color.GREEN else Color.RED
                canvas.drawText(gameState!!, width / 2f, height / 2f, paint)
            }

            // Muestra el tiempo transcurrido
            paint.color = Color.WHITE
            paint.textSize = 60f
            paint.textAlign = Paint.Align.LEFT
            val seconds = elapsedTime / 1000
            if(!win_end){
                canvas.drawText("Tiempo: $seconds s", 50f, 300f, paint)
            }


            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun resume() {
        if (thread == null || !thread!!.isAlive) {
            isPlaying = true
            thread = Thread(this)
            lastUpdateTime = System.currentTimeMillis()
            thread!!.start()
        }
    }

    fun pause() {
        try {
            isPlaying = false
            thread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    // Métodos para configurar el nivel
    fun setLevel(level: Level) {
        currentLevel = level
        userResult = null
        gameState = null
    }

    // Métodos para recibir el resultado del usuario
    fun setUserResult(result: Int) {
        userResult = result
    }

    // Métodos de movimiento
    fun moveKnightLeft() = knight.moveLeft()
    fun moveKnightRight() = knight.moveRight() // Gira hacia la derecha
    fun stopKnight() = knight.stopMoving()
    fun knightAttack() = knight.attack()

    fun moveEnemyLeft() = enemy.moveLeft()
    fun moveEnemyRight() = enemy.moveRight()
    fun stopEnemy() = enemy.stopMoving()
    fun enemyAttack() = enemy.attack()
}

// Clase para manejar los niveles
data class Level(
    val operation: String, // Enunciado de la operación (por ejemplo, "5 + 3")
    val expectedResult: Int // Resultado esperado (por ejemplo, 8)
)
