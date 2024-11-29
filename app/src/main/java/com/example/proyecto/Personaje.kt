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
    var direction = "derecha"
    var speed = 10
    var spriteXTarget: Int = 0
    var attackSound: MediaPlayer? = null

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

    fun attack(onComplete: (() -> Unit)? = null) {
        sprite.setState(Sprite.State.ATTACKING) // Cambia el estado a atacar
        sprite.frameIndex = 0 // Reinicia la animación al primer frame
        isMoving = false
        state = "atacando"
        attackSound?.start()

        // Calcular la duración total de la animación
        val attackDuration = sprite.frameDuration * sprite.currentFrames.size

        // Usar un Handler para esperar a que termine la animación
        Handler().postDelayed({
            if (state == "atacando") {
                state = "parado"
                sprite.setState(Sprite.State.IDLE) // Vuelve al estado inicial
                onComplete?.invoke() // Llama al callback si está definido
            }
        }, attackDuration.toLong())
    }



    fun moveSpriteToTarget(onComplete: (() -> Unit)? = null) {
        if (isMoving) return // Evita conflictos si ya está en movimiento
        isMoving = true
        sprite.frameDuration = 16
        val handler = android.os.Handler()
        handler.post(object : Runnable {
            override fun run() {
                if (x < spriteXTarget) {
                    x += speed
                    direction = "derecha"
                } else if (x > spriteXTarget) {
                    x -= speed
                    direction = "izquierda"
                }
                update()

                if (abs(x - spriteXTarget) > speed) {
                    handler.postDelayed(this, 16) // Continuar moviendo
                } else {
                    x = spriteXTarget.toFloat() // Asegurar posición exacta
                    isMoving = false
                    sprite.frameDuration = 90 // Restaurar velocidad de animación
                    onComplete?.invoke() // Ejecutar callback si está definido
                }
            }
        })
    }



    fun moveLeft() { isMoving = true; direction = "izquierda";state = "moviendo"}
    fun moveRight() { isMoving = true;  direction = "derecha" ;state = "moviendo"}
    fun stopMoving() { isMoving = false; state = "parado"}
}