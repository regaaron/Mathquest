package com.example.proyecto

data class Card(
    val content: Int, // Siempre será un recurso de imagen
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false
)
