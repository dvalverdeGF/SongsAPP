package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chord_accidentals")
data class ChordAccidental(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String, // Ejemplo: "#" o "b"
)
