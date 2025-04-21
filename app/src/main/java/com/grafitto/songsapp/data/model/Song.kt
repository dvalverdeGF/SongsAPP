// data/model/Song.kt
package com.grafitto.songsapp.data.model

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val verses: List<Verse> = emptyList(),
)
