package com.example.proyecto

import android.graphics.Bitmap
import android.graphics.Canvas


class Sprite(
    val idleFrames: List<Bitmap>,
    val moveFrames: List<Bitmap>,
    val attackFrames: List<Bitmap>) {

    private var frameTime = 0
    var frameDuration = 90 // Duración de cada frame en milisegundos (ajusta según necesites)

    var frameIndex = 0
    var currentFrames = idleFrames

    enum class State {
        IDLE,
        MOVING,
        ATTACKING
    }

    var state = State.IDLE
        private set // El setter es privado para que no se pueda modificar directamente

    fun setState(newState: State){
        if(state != newState){
            state = newState
            frameIndex = 0
            currentFrames = when(state){
                State.IDLE -> idleFrames
                State.MOVING -> moveFrames
                State.ATTACKING -> attackFrames
            }
        }
    }

    //Actualizar la animacion dependiendo el estado actual
    // Actualiza la animación dependiendo el estado actual
    fun update(deltaTime: Long) { // Recibe el tiempo transcurrido desde el último frame
        frameTime += deltaTime.toInt() // Suma el tiempo transcurrido
        if (frameTime >= frameDuration) { // Comprueba si ha pasado el tiempo suficiente para cambiar de frame
            frameIndex = (frameIndex + 1) % currentFrames.size
            frameTime = 0 // Resetea el contador de tiempo
        }
    }

    //    Dibuja el frame actual en el canvas
    fun draw(canvas: Canvas, x:Float, y:Float, direccion:String){
        if (direccion != "derecha") {
            // Crear una matriz para voltear la imagen horizontalmente
            val matrix = android.graphics.Matrix()
            matrix.postScale(-1f, 1f)  // Escala negativa en el eje X para voltear

            // Calcular la posición de la imagen volteada para que coincida con la original
            matrix.postTranslate(x + currentFrames[frameIndex].width, y)

            // Dibujar el Bitmap usando la matriz invertida
            canvas.drawBitmap(currentFrames[frameIndex], matrix, null)
        } else {
            // Dibuja el Bitmap normalmente si no se debe voltear
            canvas.drawBitmap(currentFrames[frameIndex], x, y, null)
        }

    }



}