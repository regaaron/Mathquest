package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.proyecto.Knight.Companion


class Enemy3(context: Context, x: Float, y: Float) :Enemy(context, x, y, worldNumber = 4) {

    companion object {
        private fun loadEnemySprite(context: Context): Sprite {
            val idleFrames = loadFrames(
                context, arrayOf(
                    R.drawable.e3parado1,
                    R.drawable.e3parado2,
                    R.drawable.e3parado3,
                    R.drawable.e3parado4,
                    R.drawable.e3parado5,
                    R.drawable.e3parado6
                )
            )

            val moveFrames = loadFrames(
                context, arrayOf(
                    R.drawable.e3move1,
                    R.drawable.e3move3,
                    R.drawable.e3move3,
                    R.drawable.e3move4,
                    R.drawable.e3move5,
                    R.drawable.e3move6
                )
            )

            val attackFrames = loadFrames(
                context, arrayOf(
                    R.drawable.e3attack1,
                    R.drawable.e3attack2,
                    R.drawable.e3attack3,
                    R.drawable.e3attack4,
                    R.drawable.e3attack5,
                    R.drawable.e3attack6,
                )
            )

            return Sprite(idleFrames, moveFrames, attackFrames)
        }
    }
}