package com.grafitto.songsapp.data.model

// Category, Author y Lyric son las clases de modelo que estamos definiendo
data class Song(
    val id: Int = 0,
    val name: String,
    val reference: String? = null,
    val author: Author? = null,
    val categories: List<Category> = emptyList(),
    val lyric: Lyric? = null,
)
