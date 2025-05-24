package com.grafitto.songsapp.data.model

// Asumiendo que LyricChord y Annotation también serán clases de modelo
data class Verse(
    val id: Int = 0,
    val text: String?,
    val orderInLyric: Int? = null,
    val lyricChords: List<LyricChord> = emptyList(),
    val annotations: List<Annotation> = emptyList(),
)
