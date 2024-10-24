package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Juego : AppCompatActivity() {

    lateinit var lienzo: CLienzo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.jugar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lienzo = findViewById(R.id.lienzo)
        val btn1 = findViewById<ImageButton>(R.id.btn1)
        val btn2 = findViewById<ImageButton>(R.id.btn2)
        val btn3 = findViewById<ImageButton>(R.id.btn3)
        val btn4 = findViewById<ImageButton>(R.id.btn4)

        btn1.setOnClickListener {
            lienzo.moverimg1()
        }

        btn2.setOnClickListener {
            lienzo.moverimg2()
        }

        btn3.setOnClickListener {
            lienzo.moverimg3()
        }

        btn4.setOnClickListener {
            lienzo.moverimg4()
        }

        val btnJugar = findViewById<Button>(R.id.btnJugar)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        btnJugar.setOnClickListener {
            Jugar()
        }

        btnSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun Jugar(){
//        cordenadas img montanas
        if(lienzo.spriteX > 0 && lienzo.spriteX <= 500 && !lienzo.isMoving ){
            val intent = Intent(this, Montanas::class.java)
            Toast.makeText(this, "pantalla1", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }

//                cordenadas img Desierto
        if(lienzo.spriteX > 520 && lienzo.spriteX <= 1020 && !lienzo.isMoving){
            val intent = Intent(this, Desierto::class.java)
            Toast.makeText(this, "pantalla2", Toast.LENGTH_SHORT).show()

            startActivity(intent)
        }

        //        cordenadas img Volcan
        if(lienzo.spriteX > 1060 && lienzo.spriteX <= 1540 && !lienzo.isMoving){
            val intent = Intent(this, Volcan::class.java)
            Toast.makeText(this, "pantalla3", Toast.LENGTH_SHORT).show()

            startActivity(intent)
        }

        //        cordenadas img Templo
        if(lienzo.spriteX > 1580 && lienzo.spriteX <= 1970 && !lienzo.isMoving ){
            val intent = Intent(this, Templo::class.java)
            Toast.makeText(this, "pantalla4", Toast.LENGTH_SHORT).show()

            startActivity(intent)
        }


    }
}