package com.grafitto.songsapp.data.model

// Verse es la clase de modelo que ya definimos
data class Lyric(
    val id: Int = 0,
    val verses: List<Verse> = emptyList(),
)
