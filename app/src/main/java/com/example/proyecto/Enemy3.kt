package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.proyecto.Knight.Companion


class Enemy3(context: Context, x: Float, y: Float) : Personaje(loadEnemySprite(context), x, y) {

    companion object {
        private const val SPRITE_WIDTH = 600
        private const val SPRITE_HEIGHT = 600

        private fun loadEnemySprite(context: Context): Sprite {
            val idleFrames = loadFrames(context, arrayOf(
                R.drawable.enemy_parado1,
                R.drawable.enemy_parado2,
                R.drawable.enemy_parado3,
                R.drawable.enemy_parado4,
                R.drawable.enemy_parado5,
                R.drawable.enemy_parado6
            ))

            val moveFrames = loadFrames(context, arrayOf(
                R.drawable.enemy_move1,
                R.drawable.enemy_move2,
                R.drawable.enemy_move3,
                R.drawable.enemy_move4,
                R.drawable.enemy_move5,
                R.drawable.enemy_move6
            ))

            val attackFrames = loadFrames(context, arrayOf(
                R.drawable.enemy_attack1,
                R.drawable.enemy_attack2,
                R.drawable.enemy_attack3,
                R.drawable.enemy_attack4,
                R.drawable.enemy_attack5,
                R.drawable.enemy_attack6,
//                R.drawable.enemy_attack7
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
    var lives: Int = 1 // Número de corazones/vidas


    override fun draw(canvas: Canvas) {
        // Dibuja el personaje usando la lógica de la clase base
        super.draw(canvas)

        // Define el número máximo de corazones por fila
        val maxHeartsPerRow = 5

        // Calcula la cantidad de filas y la cantidad de corazones en la última fila
        val fullRows = lives / maxHeartsPerRow
        val remainingHearts = lives % maxHeartsPerRow

        // Espacio entre corazones
        val heartSpacing = heartBitmap.width + 10

        // Posición de la fila superior (ajusta según la posición del personaje)
        var startY = y - heartBitmap.height - 10

        // Dibuja cada fila de corazones
        for (row in 0..fullRows) {
            val heartsInThisRow = if (row < fullRows) maxHeartsPerRow else remainingHearts
            val totalWidth = (heartsInThisRow * heartSpacing) - 10

            // Calcula el `startX` para centrar la fila actual
            val startX = x + (Enemy.SPRITE_WIDTH - totalWidth) / 2

            // Dibuja los corazones en la fila actual
            for (i in 0 until heartsInThisRow) {
                val heartX = startX + i * heartSpacing
                canvas.drawBitmap(heartBitmap, heartX, startY+200f, null)
            }

            // Desplaza `startY` para la siguiente fila
            startY -= heartBitmap.height + 10
        }
    }



}