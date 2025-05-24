package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chord_modifiers")
data class ChordModifier(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String, // Ejemplo: "maj", "add9", "sus4"
)
