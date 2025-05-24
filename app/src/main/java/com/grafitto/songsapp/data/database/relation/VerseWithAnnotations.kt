package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Annotation // Asegúrate de que Annotation.kt exista
import com.grafitto.songsapp.data.database.entity.Verse

data class VerseWithAnnotations(
    @Embedded val verse: Verse,
    @Relation(
        parentColumn = "id", // Clave primaria de Verse
        entityColumn = "verse_id", // Clave foránea en Annotation que referencia a Verse
    )
    val annotations: List<Annotation>,
)
