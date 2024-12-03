package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.proyecto.Knight.Companion


class Enemy2(context: Context, x: Float, y: Float) :Enemy(context, x, y, worldNumber = 3) {

    companion object {
        private fun loadEnemySprite(context: Context): Sprite {
            val idleFrames = loadFrames(context, arrayOf(
                R.drawable.e2parado1,
                R.drawable.e2parado2,
                R.drawable.e2parado3,
                R.drawable.e2parado4,
                R.drawable.e2parado5,
                R.drawable.e2parado6
            ))

            val moveFrames = loadFrames(context, arrayOf(
                R.drawable.e2move1,
                R.drawable.e2move2,
                R.drawable.e2move3,
                R.drawable.e2move4,
                R.drawable.e2move5,
                R.drawable.e2move6
            ))

            val attackFrames = loadFrames(context, arrayOf(
                R.drawable.e2attack1,
                R.drawable.e2attack2,
                R.drawable.e2attack3,
                R.drawable.e2attack4,
                R.drawable.e2attack5,
                R.drawable.e2attack6,
            ))

            return Sprite(idleFrames, moveFrames, attackFrames)
        }
    }
}