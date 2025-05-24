package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "annotations",
    foreignKeys = [
        ForeignKey(
            entity = AnnotationSymbol::class,
            parentColumns = ["id"],
            childColumns = ["symbolId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Verse::class,
            parentColumns = ["id"],
            childColumns = ["verseId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class Annotation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val relativeCharPosition: Float,
    val pixelOffset: Float? = null,
    val symbolId: Long, // Clave foránea a AnnotationSymbol
    val verseId: Long, // Clave foránea a Verse
)
