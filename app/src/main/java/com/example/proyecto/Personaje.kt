package com.example.proyecto

import android.graphics.Canvas
import android.media.MediaPlayer
import android.os.Handler
import kotlin.math.abs


open class Personaje(
    var sprite: Sprite,
    var x: Float,
    var y: Float
) {
    var isMoving = false
    var state = "parado"
    var direction = "deracha"
    var speed = 10
    var spriteXTarget: Int = 0


    private var lastFrameTime: Long = System.currentTimeMillis()

    fun update(){
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastFrameTime // Calcula el tiempo transcurrido
        lastFrameTime = currentTime // Actualiza el tiempo de la última actualización
        sprite.update(deltaTime)

//         Movimiento simple (puedes mejorar la lógica para direcciones específicas)
        if (isMoving) {
            sprite.setState(Sprite.State.MOVING)
            if (direction == "derecha") {
                x += speed
            } else {
                x -= speed
            }

        }

        when (state) {
            "parado" -> sprite.setState(Sprite.State.IDLE)
            "moviendo" -> sprite.setState(Sprite.State.MOVING)
            "atacando" -> sprite.setState(Sprite.State.ATTACKING)
        }
    }


    open fun draw(canvas: Canvas){
        sprite.draw(canvas,x,y,direction)
    }

    fun attack(){
        sprite.setState(Sprite.State.ATTACKING)
        sprite.frameIndex = 0
        isMoving = false
        state = "atacando"

        // Después de un retardo, regresa al estado "parado"
        Handler().postDelayed({
            if (state == "atacando") {
                state = "parado"
                sprite.setState(Sprite.State.IDLE)
            }
        }, sprite.frameDuration * sprite.currentFrames.size.toLong()) // Tiempo basado en la duración de la animación
    }


    public fun moveSpriteToTarget() {
//        if (isMoving) return
        state = "moviendo"
        // Establecer que el sprite está en movimiento
        sprite.frameDuration=16
        isMoving = true
        val handler = android.os.Handler()
        handler.post(object : Runnable {
            override fun run() {
                // Mover hacia la derecha o izquierda según la posición objetivo
                if (x < spriteXTarget) {
                    x += speed
//                    facingRight = true // Ajustar la dirección
                    direction = "derecha"
                    update()
                } else if (x > spriteXTarget) {
                    x -= speed
//                    facingRight = false // Ajustar la dirección
                    direction = "izquierda"
                    update()
                }
//                update()
                // Si el sprite aún no ha alcanzado el destino, continuar moviendo
                if (abs(x - spriteXTarget) > speed) {
                    handler.postDelayed(this, 32) // Repetir cada 16ms para suavidad (~60fps)
                    update()
                } else {
                    // Asegurar que el sprite esté exactamente en la posición objetivo
                    x = spriteXTarget.toFloat()
                    update()
                    isMoving = false
                    sprite.frameDuration=90
                    attack()
                }
            }
        })
        update()
    }


    fun moveLeft() { isMoving = true; direction = "izquierda";state = "moviendo"}
    fun moveRight() { isMoving = true;  direction = "derecha" ;state = "moviendo"}
    fun stopMoving() { isMoving = false; state = "parado"}
}