package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.AnnotationSymbol
import com.grafitto.songsapp.data.database.entity.LyricAnnotation

data class AnnotationWithSymbol(
    @Embedded val annotation: LyricAnnotation,
    @Relation(
        parentColumn = "symbol_id",
        entityColumn = "id",
    )
    val symbol: AnnotationSymbol,
)
