package com.grafitto.songsapp.data.model

data class ChordExtension(
    val id: Int = 0,
    val name: String, // Ej: "7th", "9th", "add9"
    val symbol: String, // Ej: "7", "9", "add9"
)
