package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.LyricAnnotation
import com.grafitto.songsapp.data.database.entity.LyricChord
import com.grafitto.songsapp.data.database.entity.Verse

data class VerseWithAllDetails(
    @Embedded val verse: Verse,
    @Relation(
        entity = LyricChord::class,
        parentColumn = "id",
        entityColumn = "verse_id",
    )
    val lyricChords: List<LyricChord>,
    @Relation(
        entity = LyricAnnotation::class,
        parentColumn = "id",
        entityColumn = "verse_id",
    )
    val annotations: List<LyricAnnotation>,
)
