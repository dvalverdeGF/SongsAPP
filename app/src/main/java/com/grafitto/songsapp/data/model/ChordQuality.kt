package com.grafitto.songsapp.data.model

data class ChordQuality(
    val id: Int = 0,
    val name: String, // Ej: "major", "minor", "diminished", "augmented"
    val symbol: String, // Ej: "maj", "m", "dim", "aug"
)
