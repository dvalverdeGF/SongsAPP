package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chord_roots")
data class ChordRoot(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?, // Ejemplo: "C", "D"
    val nameLatin: String?, // Ejemplo: "Do", "Re"
    // La relación OneToMany con LyricChord se maneja a través de una clave foránea en LyricChord (chordRootId)
)
