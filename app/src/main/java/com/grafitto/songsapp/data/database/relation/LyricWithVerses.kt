package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Lyric
import com.grafitto.songsapp.data.database.entity.Verse

data class LyricWithVerses(
    @Embedded val lyric: Lyric,
    @Relation(
        parentColumn = "id", // ID de Lyric
        entityColumn = "lyric_id", // Columna en Verse que referencia a Lyric
    )
    val verses: List<Verse>,
)
