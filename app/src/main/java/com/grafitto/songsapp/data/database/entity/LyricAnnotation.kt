package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lyric_annotations",
    foreignKeys = [
        ForeignKey(
            entity = AnnotationSymbol::class,
            parentColumns = ["id"],
            childColumns = ["symbol_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = Verse::class,
            parentColumns = ["id"],
            childColumns = ["verse_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["symbol_id"]),
        Index(value = ["verse_id"]),
    ],
)
data class LyricAnnotation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "relative_char_position")
    val relativeCharPosition: Float,
    @ColumnInfo(name = "pixel_offset")
    val pixelOffset: Float? = null,
    @ColumnInfo(name = "symbol_id")
    val symbolId: Long,
    @ColumnInfo(name = "verse_id")
    val verseId: Long,
)
