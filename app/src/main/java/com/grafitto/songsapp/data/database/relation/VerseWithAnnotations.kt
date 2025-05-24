package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.LyricAnnotation
import com.grafitto.songsapp.data.database.entity.Verse

data class VerseWithAnnotations(
    @Embedded val verse: Verse,
    @Relation(
        parentColumn = "id", // Clave primaria de Verse
        entityColumn = "verse_id", // Clave foránea en LyricAnnotation que referencia a Verse
    )
    val annotations: List<LyricAnnotation>,
)
