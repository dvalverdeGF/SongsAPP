package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chord_extensions")
data class ChordExtension(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String, // Ejemplo: "5", "7", "9", "13"
)
