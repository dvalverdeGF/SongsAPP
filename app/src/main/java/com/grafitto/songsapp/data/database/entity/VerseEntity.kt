package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    foreignKeys = [
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("songId")],
)
data class VerseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val songId: Int,
    val chords: String = "",
    val lyrics: String = "",
    val position: Int = 0, // Para mantener el orden
)
