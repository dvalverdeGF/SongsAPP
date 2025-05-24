package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Annotation // Asegúrate de que Annotation.kt exista y tenga 'annotationSymbolId'
import com.grafitto.songsapp.data.database.entity.AnnotationSymbol // Asegúrate de que AnnotationSymbol.kt exista

data class AnnotationWithSymbol(
    @Embedded val annotation: Annotation,
    @Relation(
        parentColumn = "annotationSymbolId", // Campo FK en Annotation.kt
        entityColumn = "id", // Campo PK en AnnotationSymbol.kt
    )
    val symbol: AnnotationSymbol,
)
