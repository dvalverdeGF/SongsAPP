package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.LyricChord
import com.grafitto.songsapp.data.database.entity.Verse

data class VerseWithLyricChords(
    @Embedded val verse: Verse,
    @Relation(
        parentColumn = "id", // ID de Verse
        entityColumn = "verse_id", // Columna en LyricChord que referencia a Verse
    )
    val lyricChords: List<LyricChord>,
)
