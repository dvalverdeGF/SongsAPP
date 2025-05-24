package com.grafitto.songsapp.data.model

data class ChordAccidental(
    val id: Int = 0,
    val name: String, // Ej: "sharp", "flat", "natural"
    val symbol: String, // Ej: "#", "b", ""
)
