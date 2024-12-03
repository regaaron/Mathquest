package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.proyecto.Knight.Companion


open class Enemy(context: Context, x: Float, y: Float,worldNumber:Int) : Personaje(loadEnemySprite(context,worldNumber), x, y) {
    companion object {
        const val SPRITE_WIDTH = 600
        private const val SPRITE_HEIGHT = 600

        private fun loadEnemySprite(context: Context, worldNumber: Int): Sprite {
            val idleFrames: List<Bitmap>
            val moveFrames: List<Bitmap>
            val attackFrames: List<Bitmap>

            when (worldNumber) {
                2 -> {
                    // Mundo 1 - Texturas básicas
                    idleFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e1parado1,
                            R.drawable.e1parado2,
                            R.drawable.e1parado3,
                            R.drawable.e1parado4,
                            R.drawable.e1parado5,
                            R.drawable.e1parado6
                        )
                    )
                    moveFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e1move1,
                            R.drawable.e1move2,
                            R.drawable.e1move3,
                            R.drawable.e1move4,
                            R.drawable.e1move5,
                            R.drawable.e1move6
                        )
                    )
                    attackFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e1attack1,
                            R.drawable.e1attack2,
                            R.drawable.e1attack3,
                            R.drawable.e1attack4,
                            R.drawable.e1attack5,
                            R.drawable.e1attack6
                        )
                    )
                }
                3 -> {
                    // Mundo 2 - Texturas avanzadas
                    idleFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e2parado1,
                            R.drawable.e2parado2,
                            R.drawable.e2parado3,
                            R.drawable.e2parado4,
                            R.drawable.e2parado5,
                            R.drawable.e2parado6
                        )
                    )
                    moveFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e2move1,
                            R.drawable.e2move2,
                            R.drawable.e2move3,
                            R.drawable.e2move4,
                            R.drawable.e2move5,
                            R.drawable.e2move6
                        )
                    )
                    attackFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e2attack1,
                            R.drawable.e2attack2,
                            R.drawable.e2attack3,
                            R.drawable.e2attack4,
                            R.drawable.e2attack5,
                            R.drawable.e2attack6
                        )
                    )
                }
                4 -> {
                    // Mundo 1 - Texturas básicas
                    idleFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e3parado1,
                            R.drawable.e3parado2,
                            R.drawable.e3parado3,
                            R.drawable.e3parado4,
                            R.drawable.e3parado5,
                            R.drawable.e3parado6
                        )
                    )
                    moveFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e3move3,
                            R.drawable.e3move2,
                            R.drawable.e3move3,
                            R.drawable.e3move4,
                            R.drawable.e3move5,
                            R.drawable.e3move6
                        )
                    )
                    attackFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.e3attack1,
                            R.drawable.e3attack2,
                            R.drawable.e3attack3,
                            R.drawable.e3attack4,
                            R.drawable.e3attack5,
                            R.drawable.e3attack6
                        )
                    )
                }
                else -> {
                    // Mundo por defecto - Texturas genéricas
                    idleFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.enemy_parado1,
                            R.drawable.enemy_parado2,
                            R.drawable.enemy_parado3,
                            R.drawable.enemy_parado4,
                            R.drawable.enemy_parado5,
                            R.drawable.enemy_parado6
                        )
                    )
                    moveFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.enemy_move1,
                            R.drawable.enemy_move2,
                            R.drawable.enemy_move3,
                            R.drawable.enemy_move4,
                            R.drawable.enemy_move5,
                            R.drawable.enemy_move6
                        )
                    )
                    attackFrames = loadFrames(
                        context, arrayOf(
                            R.drawable.enemy_attack1,
                            R.drawable.enemy_attack2,
                            R.drawable.enemy_attack3,
                            R.drawable.enemy_attack4,
                            R.drawable.enemy_attack5,
                            R.drawable.enemy_attack6
                        )
                    )
                }
            }
            return Sprite(idleFrames, moveFrames, attackFrames)
        }

        fun loadFrames(context: Context, resourceIds: Array<Int>): List<Bitmap> {
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