package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class Enemy(context: Context, x: Float, y: Float) : Personaje(loadEnemySprite(context), x, y) {

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
}