package com.example.proyecto

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import com.example.proyecto.Knight.Companion


class Enemy1(context: Context, x: Float, y: Float) : Enemy(context, x, y, worldNumber = 2) {
    companion object {
        fun loadEnemySprite(context: Context): Sprite {
            val idleFrames = loadFrames(context, arrayOf(
                R.drawable.e1parado1,
                R.drawable.e1parado2,
                R.drawable.e1parado3,
                R.drawable.e1parado4,
                R.drawable.e1parado5,
                R.drawable.e1parado6
            ))
            val moveFrames = loadFrames(context, arrayOf(
                R.drawable.e1move1,
                R.drawable.e1move2,
                R.drawable.e1move3,
                R.drawable.e1move4,
                R.drawable.e1move5,
                R.drawable.e1move6
            ))
            val attackFrames = loadFrames(context, arrayOf(
                R.drawable.e1attack1,
                R.drawable.e1attack2,
                R.drawable.e1attack3,
                R.drawable.e1attack4,
                R.drawable.e1attack5,
                R.drawable.e1attack6
            ))
            return Sprite(idleFrames, moveFrames, attackFrames)
        }
    }
}