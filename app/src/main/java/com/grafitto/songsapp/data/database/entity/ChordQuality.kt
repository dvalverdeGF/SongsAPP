package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chord_qualities")
data class ChordQuality(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val code: String, // Ejemplo: "", "m"
)
