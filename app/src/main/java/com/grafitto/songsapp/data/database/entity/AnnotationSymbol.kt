package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "annotation_symbols")
data class AnnotationSymbol(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val symbolName: String?, // Ejemplo de propiedad, ajusta según tus necesidades
    val symbolContent: String?, // Ejemplo de propiedad, ajusta según tus necesidades
)
