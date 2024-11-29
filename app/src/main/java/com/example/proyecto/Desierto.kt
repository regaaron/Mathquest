package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
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

class Desierto : AppCompatActivity() {

    lateinit var lienzo: CLienzo
    lateinit var progresoDBHelper: SQLiteHelper
    // Variables para los niveles, inicializadas como bloqueados (-1)
    var lvl1 = -1
    var lvl2 = -1
    var lvl3 = -1
    var lvl4 = -1
    var lvl5 = -1
    var jugar=1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.desierto)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progresoDBHelper = SQLiteHelper(this)

// Consultas para obtener los valores de los niveles
        val db = progresoDBHelper.readableDatabase
        val cursor = db.rawQuery("SELECT lvl1, lvl2, lvl3, lvl4, lvl5 FROM progreso WHERE id = 2", null)

        val tvNiveles = findViewById<TextView>(R.id.tvNiveles)



// Verificamos si hay resultados en el cursor
        if (cursor.moveToFirst()) {
            // Obtenemos los valores de cada nivel
            lvl1 = if (cursor.isNull(cursor.getColumnIndex("lvl1"))) -1 else cursor.getInt(cursor.getColumnIndex("lvl1"))
            lvl2 = if (cursor.isNull(cursor.getColumnIndex("lvl2"))) -1 else cursor.getInt(cursor.getColumnIndex("lvl2"))
            lvl3 = if (cursor.isNull(cursor.getColumnIndex("lvl3"))) -1 else cursor.getInt(cursor.getColumnIndex("lvl3"))
            lvl4 = if (cursor.isNull(cursor.getColumnIndex("lvl4"))) -1 else cursor.getInt(cursor.getColumnIndex("lvl4"))
            lvl5 = if (cursor.isNull(cursor.getColumnIndex("lvl5"))) -1 else cursor.getInt(cursor.getColumnIndex("lvl5"))

            // Mostramos los valores en el TextView
            tvNiveles.text = """
        Nivel 1: $lvl1 - Nivel 2: $lvl2
        Nivel 3: $lvl3 - Nivel 4: $lvl4
        Nivel 5: $lvl5
    """.trimIndent()
        } else {
            tvNiveles.text = "No se encontraron niveles."
        }

        cursor.close()
        db.close()

// Actualizamos las estrellas para cada nivel
        actualizarEstrellas(1, lvl1)
        actualizarEstrellas(2, lvl2)
        actualizarEstrellas(3, lvl3)
        actualizarEstrellas(4, lvl4)
        actualizarEstrellas(5, lvl5)

        BloquerNiveles()


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
                if(lvl2 == -1){
                    Toast.makeText(this, "Nivel bloqueado", Toast.LENGTH_SHORT).show()

                }else{
                    lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                    lienzo.moveSpriteToTarget()
                    jugar=2
                }

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
                if(lvl3 == -1){
                    Toast.makeText(this, "Nivel bloqueado", Toast.LENGTH_SHORT).show()
                }else {
                    lienzo.spriteXTarget = x.toFloat() + with / 2 - lienzo.spriteWidth / 2
                    lienzo.moveSpriteToTarget()
                    jugar = 3
                }
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
                if (lvl4 == -1) {
                    Toast.makeText(this, "Nivel bloqueado", Toast.LENGTH_SHORT).show()
                }else{
                    lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                    lienzo.moveSpriteToTarget()
                    jugar=4
                }

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
                if(lvl5 == -1){
                    Toast.makeText(this, "Nivel bloqueado", Toast.LENGTH_SHORT).show()
                }else{
                    lienzo.spriteXTarget=x.toFloat() + with/2 - lienzo.spriteWidth/2
                    lienzo.moveSpriteToTarget()
                    jugar=5
                }

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

    fun BloquerNiveles(){
        if(lvl2 == -1){
            var tvlvl2 = findViewById<TextView>(R.id.tvNivel2)
            tvlvl2.text = "X"
        }
        if(lvl3 == -1){
            var tvlvl3 = findViewById<TextView>(R.id.tvNivel3)
            tvlvl3.text = "X"
        }
        if(lvl4 == -1){
            var tvlvl4 = findViewById<TextView>(R.id.tvNivel4)
            tvlvl4.text = "X"
        }
        if(lvl5 == -1){
            var tvlvl5 = findViewById<TextView>(R.id.tvNivel5)
            tvlvl5.text = "X"
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
        val intent = Intent(this, Resta::class.java)
        intent.putExtra("nivel", nivel) // Enviamos el nivel como extra
        startActivity(intent)
    }

    fun actualizarEstrellas(nivel: Int, estrellas: Int) {
        // Identifica los ImageView del nivel
        val estrella1 = findViewById<ImageView>(resources.getIdentifier("estrella1_btn$nivel", "id", packageName))
        val estrella2 = findViewById<ImageView>(resources.getIdentifier("estrella2_btn$nivel", "id", packageName))
        val estrella3 = findViewById<ImageView>(resources.getIdentifier("estrella3_btn$nivel", "id", packageName))

        // Configura las estrellas según el progreso
        when (estrellas) {
            -1 -> {
                estrella1.setImageResource(R.drawable.estrella_gris)
                estrella2.setImageResource(R.drawable.estrella_gris)
                estrella3.setImageResource(R.drawable.estrella_gris)
            }
            0 -> {
                estrella1.setImageResource(R.drawable.estrella_gris)
                estrella2.setImageResource(R.drawable.estrella_gris)
                estrella3.setImageResource(R.drawable.estrella_gris)
            }
            1 -> {
                estrella1.setImageResource(R.drawable.estrella)
                estrella2.setImageResource(R.drawable.estrella_gris)
                estrella3.setImageResource(R.drawable.estrella_gris)
            }
            2 -> {
                estrella1.setImageResource(R.drawable.estrella)
                estrella2.setImageResource(R.drawable.estrella)
                estrella3.setImageResource(R.drawable.estrella_gris)
            }
            3 -> {
                estrella1.setImageResource(R.drawable.estrella)
                estrella2.setImageResource(R.drawable.estrella)
                estrella3.setImageResource(R.drawable.estrella)
            }
        }
    }


}