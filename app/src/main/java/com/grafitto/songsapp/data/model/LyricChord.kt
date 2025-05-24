package com.grafitto.songsapp.data.model

// Se importan las clases de modelo para los componentes del acorde
import com.grafitto.songsapp.data.model.ChordAccidental
import com.grafitto.songsapp.data.model.ChordExtension
import com.grafitto.songsapp.data.model.ChordModifier
import com.grafitto.songsapp.data.model.ChordQuality
import com.grafitto.songsapp.data.model.ChordRoot
import com.grafitto.songsapp.data.model.ChordVariant

data class LyricChord(
    val id: Int = 0,
    val relativeCharPosition: Float,
    val pixelOffset: Float? = null,
    // Se utilizan las clases de modelo para los componentes del acorde
    val chordRoot: ChordRoot,
    val accidental: ChordAccidental? = null,
    val quality: ChordQuality? = null,
    val extension: ChordExtension? = null,
    val modifier: ChordModifier? = null,
    val variant: ChordVariant? = null,
)
