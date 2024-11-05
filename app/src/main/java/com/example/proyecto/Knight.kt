package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas


class Knight(context: Context, x: Float, y: Float) : Personaje(loadKnightSprite(context), x, y) {

    companion object {
        private const val SPRITE_WIDTH = 300
        private const val SPRITE_HEIGHT = 300

        private fun loadKnightSprite(context: Context): Sprite {
            val idleFrames = loadFrames(context, arrayOf(
                R.drawable.parado1,
                R.drawable.parado2,
                R.drawable.parado3,
                R.drawable.parado4,
                R.drawable.parado5,
                R.drawable.parado6,
                R.drawable.parado7,
                R.drawable.parado8
            ))

            val moveFrames = loadFrames(context, arrayOf(
                R.drawable.img1,
                R.drawable.img2,
                R.drawable.img3,
                R.drawable.img4,
                R.drawable.img5,
                R.drawable.img6,
                R.drawable.img7,
                R.drawable.img8,
                R.drawable.img9,
                R.drawable.img10
            ))

            val attackFrames = loadFrames(context, arrayOf(
                R.drawable.ataque1,
                R.drawable.ataque2,
                R.drawable.ataque4,
                R.drawable.ataque5,
                R.drawable.ataque6,
                R.drawable.ataque7,
                R.drawable.ataque8,
                R.drawable.ataque9,
                R.drawable.ataque10
            ))

            return Sprite(idleFrames, moveFrames, attackFrames)
        }

        private fun loadFrames(context: Context, resourceIds: Array<Int>): List<Bitmap> {
            return resourceIds.mapNotNull { resourceId ->
                BitmapFactory.decodeResource(context.resources, resourceId)?.let {
                    Bitmap.createScaledBitmap(it, SPRITE_WIDTH, SPRITE_HEIGHT, true)
                }
            }
        }

    }

    // Bitmap para el corazón y el número de vidas
    // Tamaño deseado para los corazones
    private val heartSize = 80 // Ajusta este valor para cambiar el tamaño del corazón

    // Bitmap para el corazón, redimensionado al tamaño deseado
    private val heartBitmap: Bitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.resources, R.drawable.heart),
        heartSize,
        heartSize,
        true
    )
    var lives: Int = 3 // Número de corazones/vidas

    override fun draw(canvas: Canvas) {
        // Dibuja el personaje usando la lógica de la clase base
        super.draw(canvas)

        // Espacio entre corazones
        val heartSpacing = heartBitmap.width + 10

        // Calcula el ancho total de los corazones para centrar
        val totalWidth = (lives * heartSpacing) - 10  // Resta un espaciado extra al final

        // Calcula la posición inicial (centrada) del primer corazón
        val startX = x + (SPRITE_WIDTH - totalWidth) / 2

        for (i in 0 until lives) {
            val heartX = startX + i * heartSpacing
            val heartY = y - heartBitmap.height - 10 // Ajusta la posición sobre el caballero
            canvas.drawBitmap(heartBitmap, heartX, heartY, null)
        }
    }
}