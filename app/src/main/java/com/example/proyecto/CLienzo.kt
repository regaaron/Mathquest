package com.example.proyecto

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.provider.MediaStore.Audio.Media
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Button
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

class CLienzo : View {

    private var extraCanvas: Canvas? = null
    private var extraBitmap: Bitmap? = null
    private var backgroundColor: Int = 0

    // Posiciones para el movimiento
    public var spriteX = 200f  // Posición X del sprite
    public var spriteY = 80f  // Posición Y del sprite
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    public var isMoving = false

    // Sprites (frames)
    private lateinit var spriteImages: List<Bitmap>
    private var currentFrame = 0
    private val frameCount = 10 // Número de frames

    // Dimensiones deseadas del sprite
    public var spriteWidth = 200f // Ancho deseado
    public var spriteHeight = 200f // Alto deseado

    private var paint: Paint = Paint()
    private var path: Path = Path()

    private var currentX = 0f
    private var currentY = 0f
    private var touchTolerance: Int = ViewConfiguration.get(context).scaledTouchSlop

    private lateinit var sonidoCorrer: MediaPlayer
    // Variable para controlar la dirección (true = derecha, false = izquierda)
    private var facingRight = true

    constructor(context: Context?) : super(context) {
        sonidoCorrer = MediaPlayer.create(context, R.raw.running)
        inicializa()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        sonidoCorrer = MediaPlayer.create(context, R.raw.running)
        inicializa()
    }

    private fun inicializa() {
        // Asignar color de fondo
        backgroundColor = Color.TRANSPARENT
        paint.isAntiAlias = true

        // Cargar las imágenes de los recursos
        spriteImages = listOf(
            BitmapFactory.decodeResource(resources, R.drawable.img1),
            BitmapFactory.decodeResource(resources, R.drawable.img2),
            BitmapFactory.decodeResource(resources, R.drawable.img3),
            BitmapFactory.decodeResource(resources, R.drawable.img4),
            BitmapFactory.decodeResource(resources, R.drawable.img5),
            BitmapFactory.decodeResource(resources, R.drawable.img6),
            BitmapFactory.decodeResource(resources, R.drawable.img7),
            BitmapFactory.decodeResource(resources, R.drawable.img8),
            BitmapFactory.decodeResource(resources, R.drawable.img9),
            BitmapFactory.decodeResource(resources, R.drawable.img10)
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Dibujar el contenido final
        extraBitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        // Dibujar el sprite actual en su posición con el tamaño ajustado y rotación según la dirección
        val sprite = spriteImages[currentFrame]
        val transformedSprite = scaleAndRotateBitmap(sprite, spriteWidth, spriteHeight, facingRight)

        paint.textSize = 50f // Tamaño de fuente, por ejemplo, 50f para un texto grande
        canvas.drawText("x: ${spriteX.toInt()}, y: ${spriteY.toInt()}", 10f, 50f, paint)
        canvas.drawBitmap(transformedSprite, spriteX, spriteY, null)
    }

    private fun scaleAndRotateBitmap(bitmap: Bitmap, width: Float, height: Float, facingRight: Boolean): Bitmap {
        val matrix = Matrix()

        // Escalar el bitmap
        val scaleX = width / bitmap.width
        val scaleY = height / bitmap.height
        matrix.postScale(scaleX, scaleY)

        // Si el personaje no está mirando a la derecha, rotarlo 180 grados para que mire hacia la izquierda
        if (!facingRight) {
            matrix.postScale(-1f, 1f, width / 2, height / 2) // Refleja en el eje X
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }

        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)

        if (dx >= touchTolerance || dy >= touchTolerance) {
            // Deslizar a la derecha
            if (motionTouchEventX > currentX) {
                moveSpriteRight()
            }
            // Deslizar a la izquierda
            else if (motionTouchEventX < currentX) {
                moveSpriteLeft()
            }

            currentX = motionTouchEventX
            currentY = motionTouchEventY
        }

        invalidate()
    }

    private fun touchUp() {
        // Aquí puedes agregar lógica si necesitas realizar acciones adicionales cuando se levanta el dedo
        path.reset()
    }

    // Funciones para mover el sprite
    private fun moveSpriteRight() {

        if(spriteX>=1940){
            spriteX=1940f
        }
        spriteX += 20f // Mover hacia la derecha
        facingRight = true // Actualizar dirección
        updateFrame()  // Cambiar al siguiente frame
    }

    private fun moveSpriteLeft() {
        if(spriteX<=0){
            spriteX=0f
        }
        spriteX -= 20f // Mover hacia la izquierda
        facingRight = false // Actualizar dirección
        updateFrame()  // Cambiar al siguiente frame
    }

    // Función para actualizar el frame del sprite
    private fun updateFrame() {
        currentFrame = (currentFrame + 1) % frameCount
    }

    // Variables para controlar el movimiento del sprite
    public var spriteXTarget: Float = 0f
    val movementSpeed: Float = 15f // Velocidad de movimiento (ajustable)

    // Función para mover el sprite hacia una posición de destino
    public fun moveSpriteToTarget() {
        if (isMoving) return


        // Reinicia el MediaPlayer si ya fue detenido
        if (!sonidoCorrer.isPlaying) {
            sonidoCorrer.reset()  // Reinicia el MediaPlayer
            sonidoCorrer = MediaPlayer.create(context, R.raw.running)  // Carga el sonido nuevamente
            sonidoCorrer.isLooping = false  // Asegura que no se repita indefinidamente
        }

        // Ajusta el volumen (valores entre 0.0 y 1.0)
        sonidoCorrer.setVolume(1.0f, 1.0f)

        // Inicia la reproducción del sonido
        sonidoCorrer.start()
        // Establecer que el sprite está en movimiento
        isMoving = true
        val handler = android.os.Handler()
        handler.post(object : Runnable {
            override fun run() {
                // Mover hacia la derecha o izquierda según la posición objetivo
                if (spriteX < spriteXTarget) {
                    spriteX += movementSpeed
                    facingRight = true // Ajustar la dirección
                    invalidate()
                } else if (spriteX > spriteXTarget) {
                    spriteX -= movementSpeed
                    facingRight = false // Ajustar la dirección
                    invalidate()
                }
                updateFrame()
                // Si el sprite aún no ha alcanzado el destino, continuar moviendo
                if (abs(spriteX - spriteXTarget) > movementSpeed) {

                    handler.postDelayed(this, 16) // Repetir cada 16ms para suavidad (~60fps)
                } else {
                    // Asegurar que el sprite esté exactamente en la posición objetivo
                    spriteX = spriteXTarget
                    invalidate()
                    // Detén el sonido
                    sonidoCorrer.stop()

                    // Reinicia el MediaPlayer para la próxima vez que se quiera reproducir
                    sonidoCorrer.prepare()

                    isMoving = false
                }
            }
        })
    }

    // Funciones que serán llamadas al presionar los botones
    fun moverimg1() {
        spriteXTarget = 220f // Posición 1
        moveSpriteToTarget()
    }

    fun moverimg2() {
        spriteXTarget = 740f // Posición 2
        moveSpriteToTarget()
    }

    fun moverimg3() {
        spriteXTarget = 1300f // Posición 3
        moveSpriteToTarget()
    }

    fun moverimg4() {
        spriteXTarget = 1820f // Posición 4
        moveSpriteToTarget()
    }

}