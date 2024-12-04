package com.example.proyecto

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Memorama : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CardAdapter
    private val cards = mutableListOf<Card>()
    private var flippedCard: Card? = null
    private var isProcessing: Boolean = false // Evita más clics mientras se procesan cartas
    var matchSound: MediaPlayer? = null
    var touchSound: MediaPlayer? = null
    var wrongSound: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memorama)
        matchSound = MediaPlayer.create(this, R.raw.match)
        touchSound= MediaPlayer.create(this, R.raw.click)
        wrongSound = MediaPlayer.create(this, R.raw.wrong)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 4) // Configura la cuadrícula de 4 columnas

        val restartButton: Button = findViewById(R.id.restartButton)
        val backButton: Button = findViewById(R.id.backButton)

        restartButton.setOnClickListener { restartGame() }
        backButton.setOnClickListener { goBack() }

        setupGame()
    }

    private fun setupGame() {
        // Definir los pares (imágenes de números y animales)
        val pairs = listOf(
            Pair(R.drawable.i1, R.drawable.img1_1),
            Pair(R.drawable.i2, R.drawable.img2_2),
            Pair(R.drawable.i3, R.drawable.img3_3),
            Pair(R.drawable.i4, R.drawable.img4_4),
            Pair(R.drawable.i5, R.drawable.img5_5),
            Pair(R.drawable.i6, R.drawable.img6_6),
            Pair(R.drawable.i7, R.drawable.img7_7),
            Pair(R.drawable.i8, R.drawable.img8_8)
        )

        // Crear las cartas mezclando números y animales
        cards.clear()
        cards.addAll((pairs.flatMap { listOf(Card(it.first), Card(it.second)) }).shuffled())

        adapter = CardAdapter(cards) { onCardClicked(it) }
        recyclerView.adapter = adapter
    }

    private fun onCardClicked(card: Card) {
        touchSound?.start()
        if (isProcessing || card.isFaceUp || card.isMatched) return

        card.isFaceUp = true
        adapter.notifyItemChanged(cards.indexOf(card))

        if (flippedCard == null) {
            flippedCard = card
        } else {
            isProcessing = true
            if (isMatch(flippedCard!!, card)) {
                card.isMatched = true
                matchSound?.start()
                flippedCard?.isMatched = true
                flippedCard = null
                isProcessing = false
            } else {
                wrongSound?.start()
                Handler(Looper.getMainLooper()).postDelayed({
                    card.isFaceUp = false
                    flippedCard?.isFaceUp = false
                    flippedCard = null
                    isProcessing = false
                    adapter.notifyDataSetChanged()
                }, 1000)
            }
        }
    }

    private fun isMatch(card1: Card, card2: Card): Boolean {
        val pairs = mapOf(
            R.drawable.i1 to R.drawable.img1_1,
            R.drawable.i2 to R.drawable.img2_2,
            R.drawable.i3 to R.drawable.img3_3,
            R.drawable.i4 to R.drawable.img4_4,
            R.drawable.i5 to R.drawable.img5_5,
            R.drawable.i6 to R.drawable.img6_6,
            R.drawable.i7 to R.drawable.img7_7,
            R.drawable.i8 to R.drawable.img8_8
        )
        return pairs[card1.content] == card2.content || pairs[card2.content] == card1.content
    }

    private fun restartGame() {
        setupGame() // Reinicia el juego creando nuevas cartas
    }

    private fun goBack() {
        // Lanza una nueva actividad (por ejemplo, MainMenuActivity)
        val intent = Intent(this, MenuMiniJuegos::class.java)
        startActivity(intent)
        finish()
    }
}