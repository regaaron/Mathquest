package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Level

class Montanas : AppCompatActivity() {

    lateinit var lienzo: CLienzo
    var jugar=1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.montana)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lienzo = findViewById(R.id.lienzo)

        var btn1 = findViewById<ImageButton>(R.id.btn1)
        var btn2 = findViewById<ImageButton>(R.id.btn2)
        var btn3 = findViewById<ImageButton>(R.id.btn3)
        var btn4 = findViewById<ImageButton>(R.id.btn4)
        var btn5 = findViewById<ImageButton>(R.id.btn5)
        var btnSalir = findViewById<Button>(R.id.btnSalir)
        var btnJugar = findViewById<Button>(R.id.btnJugar)

        btn1.post{
            val with =btn1.width
            val height = btn1.height
            println("Button1:")
            println("Width: $with, Height: $height")

            val location = IntArray(2)
            btn1.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            println("Button1:")
            println("X: $x, Y: $y")

            lienzo.spriteX=x.toFloat()+with/2
            lienzo.spriteY=y.toFloat() - lienzo.spriteHeight-40f
            lienzo.invalidate()

            btn1.setOnClickListener {
                lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                lienzo.moveSpriteToTarget()
                jugar=1
            }
        }

        btn2.post{
            val with =btn2.width
            val height = btn2.height
            println("Button2:")
            println("Width: $with, Height: $height")

            val location = IntArray(2)
            btn2.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            println("Button2:")
            println("X: $x, Y: $y")

            btn2.setOnClickListener {
                lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                lienzo.moveSpriteToTarget()
                jugar=2
            }

        }

        btn3.post{
            val with =btn3.width
            val height = btn3.height
            println("Button3:")
            println("Width: $with, Height: $height")

            val location = IntArray(2)
            btn3.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            println("Button3:")
            println("X: $x, Y: $y")

            btn3.setOnClickListener {
                lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                lienzo.moveSpriteToTarget()
                jugar=3
            }

        }

        btn4.post{
            val with =btn4.width
            val height = btn4.height
            println("Button4:")
            println("Width: $with, Height: $height")

            val location = IntArray(2)
            btn4.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            println("Button4:")
            println("X: $x, Y: $y")

            btn4.setOnClickListener {
                lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                lienzo.moveSpriteToTarget()
                jugar=4
            }

        }

        btn5.post{
            val with =btn5.width
            val height = btn5.height
            println("Button5:")
            println("Width: $with, Height: $height")

            val location = IntArray(2)
            btn5.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]

            println("Button5:")
            println("X: $x, Y: $y")

            btn5.setOnClickListener {
                lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                lienzo.moveSpriteToTarget()
                jugar=5
            }
        }

        btnJugar.setOnClickListener {
            Jugar()
        }
        btnSalir.setOnClickListener {
            val intent = Intent(this, Juego::class.java)
            startActivity(intent)
        }

    }

    fun Jugar(){
        // Si el personaje se está moviendo
        if (lienzo.isMoving) {
            // Lanza una coroutine en el contexto del Main (UI thread)
            GlobalScope.launch(Dispatchers.Main) {
                // Espera hasta que el personaje deje de moverse
                while (lienzo.isMoving) {
                    delay(100) // Espera 100 ms entre cada verificación
                }
            }
        } else {
            when (jugar) {
                1 -> startSumaActivity(1)
                2 -> startSumaActivity(2)
                3 -> startSumaActivity(3)
                4 -> startSumaActivity(4)
                5 -> startSumaActivity(5)
            }
        }
    }

    fun startSumaActivity(nivel: Int) {
        val intent = Intent(this, Suma1::class.java)
        intent.putExtra("nivel", nivel) // Enviamos el nivel como extra
        startActivity(intent)
    }

}