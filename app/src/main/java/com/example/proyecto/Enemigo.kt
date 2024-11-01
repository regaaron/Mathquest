package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect

class Enemigo(context: Context) {
    private val sprite: Bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.tnt_red)
    private val frameWidth = sprite.width / 7 // Suponiendo que el sprite tiene 7 columnas
    private val frameHeight = sprite.height / 3 // Suponiendo que el sprite tiene 3 filas
    private var x: Float = 900f // Posición inicial X del enemigo
    private var y: Float = 200f // Posición inicial Y del enemigo
    private var currentFrame: Int = 0 // Frame actual para la animación

    // Método para cambiar el frame actual
    fun setFrame(frame: Int) {
        currentFrame = frame % 21 // 21 = 7 columnas * 3 filas
    }

    // Este método dibuja el enemigo en el canvas principal
    fun dibujar(canvas: Canvas) {
        // Calcular la fila y la columna del frame actual
        val fila = currentFrame / 7 // Cada fila tiene 7 frames
        val columna = currentFrame % 7 // Restante determina la columna

        // Calcula el rectángulo de origen para el frame actual
        val sourceRect = Rect(
            columna * frameWidth, // X de la columna
            fila * frameHeight,    // Y de la fila
            (columna + 1) * frameWidth,
            (fila + 1) * frameHeight
        )

        // Define el rectángulo de destino para ajustar el tamaño deseado
        val destRect = Rect(x.toInt(), y.toInt(), (x + 1000f).toInt(), (y + 1000f).toInt())

        // Dibuja el bitmap en el canvas
        canvas.drawBitmap(sprite, sourceRect, destRect, null)
    }
}
