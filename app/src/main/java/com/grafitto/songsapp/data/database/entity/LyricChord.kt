package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lyric_chords",
    foreignKeys = [
        ForeignKey(
            entity = Verse::class,
            parentColumns = ["id"],
            childColumns = ["verse_id"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ChordRoot::class, // Asumiendo que tienes una entidad ChordRoot.kt
            parentColumns = ["id"],
            childColumns = ["chord_root_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["verse_id"]), Index(value = ["chord_root_id"])],
)
data class LyricChord(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "relative_char_position")
    val relativeCharPosition: Float,
    @ColumnInfo(name = "pixel_offset")
    val pixelOffset: Float? = null,
    @ColumnInfo(name = "verse_id")
    val verseId: Long, // Clave foránea a Verse
    @ColumnInfo(name = "chord_root_id")
    val chordRootId: Long, // Clave foránea a ChordRoot (en PHP se llama 'chord')
)
