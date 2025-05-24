package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "annotation_symbols")
data class AnnotationSymbol(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String, // Ejemplo: "p", "𝆏"
    val name: String, // Ejemplo: "piano (suave)"
    // La relación OneToMany con Annotation se maneja a través de una clave foránea en Annotation (symbolId)
)
