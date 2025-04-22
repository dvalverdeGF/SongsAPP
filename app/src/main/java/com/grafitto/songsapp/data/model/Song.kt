// data/model/Song.kt
package com.grafitto.songsapp.data.model

data class Song(
    val id: Int = 0,
    val title: String,
    val artist: String,
    val referenceNumber: String = "",
    val verses: List<Verse> = emptyList(),
    val categories: List<Category> = emptyList(),
)
