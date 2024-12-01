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
import kotlinx.coroutines.delay
import kotlinx.coroutines.*

class Juego : AppCompatActivity() {

    lateinit var lienzo: CLienzo
    var jugar=1;
    lateinit var progresoDBHelper: SQLiteHelper
    var intent1:Intent?=null

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
        progresoDBHelper = SQLiteHelper(this)


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
        // Si el personaje se está moviendo
        if (lienzo.isMoving) {
            // Lanza una coroutine en el contexto del Main (UI thread)
            GlobalScope.launch(Dispatchers.Main) {
                // Espera hasta que el personaje deje de moverse
                while (lienzo.isMoving) {
                    delay(100) // Espera 100 ms entre cada verificación
                }

                // Ahora que el personaje ha dejado de moverse, llama a la función de la pantalla siguiente
                when (jugar) {
                    1 -> Montanas()
                    2 -> Desierto()
                    3 -> Volcan()
                    4 -> Templo()
                }
            }
        } else {
            // Si el personaje no se está moviendo, procede directamente
            when (jugar) {
                1 -> Montanas()
                2 -> Desierto()
                3 -> Volcan()
                4 -> Templo()
            }
        }
    }

    fun Montanas(){
        if(progresoDBHelper.tutorialHecho(1))
            this.intent1 = Intent(this, Suma1::class.java)
        else
            this.intent1 = Intent(this, PreSuma::class.java)
        Toast.makeText(this, "pantalla1", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    fun Desierto(){
        if(progresoDBHelper.tutorialHecho(2))
            this.intent1 = Intent(this, Resta::class.java)
        else
            this.intent1 = Intent(this, PreResta::class.java)
        Toast.makeText(this, "pantalla2", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    fun Volcan(){
        if(progresoDBHelper.tutorialHecho(3))
            this.intent1 = Intent(this, Multiplicacion::class.java)
        else
            this.intent1 = Intent(this, PreMultiplicacion::class.java)
        Toast.makeText(this, "pantalla3", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    fun Templo(){
        if(progresoDBHelper.tutorialHecho(1))
            this.intent1 = Intent(this, Division::class.java)
        else
            this.intent1 = Intent(this, PreDivision::class.java)
        Toast.makeText(this, "pantalla4", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }




}