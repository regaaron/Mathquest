package com.example.proyecto

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
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

        // Actualizar lógica del juego aquí
        knight.update()
        enemy.update()
    }

    fun draw() {
        if(holder.surface.isValid){
            val canvas = holder.lockCanvas()

            // Dibuja el fondo transparente en lugar de un color sólido
            canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR)

            knight.draw(canvas)
            enemy.draw(canvas)

            // Configura la pintura para el tiempo transcurrido
            paint.color = Color.WHITE
            paint.textSize = 60f

            // Muestra el tiempo en segundos en la esquina superior izquierda
            val seconds = elapsedTime / 1000
            canvas.drawText("Tiempo: $seconds s", 50f, 100f, paint)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun resume() {
        if (thread == null || !thread!!.isAlive) {
            isPlaying = true
            thread = Thread(this)

            // Restablecer lastUpdateTime al reanudar para ignorar el tiempo en pausa
            lastUpdateTime = System.currentTimeMillis()

            thread!!.start()
        }
    }

    fun pause() {
        try {
            isPlaying = false
            thread?.join() // Detener el hilo

        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }


    fun moveKnightLeft() = knight.moveLeft()
    fun moveKnightRight() = knight.moveRight()
    fun stopKnight() = knight.stopMoving()
    fun knightAttack() = knight.attack()

    fun moveEnemyLeft() = enemy.moveLeft()
    fun moveEnemyRight() = enemy.moveRight()
    fun stopEnemy() = enemy.stopMoving()
    fun enemyAttack() = enemy.attack()
}
