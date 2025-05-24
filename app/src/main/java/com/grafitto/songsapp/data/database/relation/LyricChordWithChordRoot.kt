package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.ChordRoot // Asegúrate de que ChordRoot.kt exista
import com.grafitto.songsapp.data.database.entity.LyricChord

data class LyricChordWithChordRoot(
    @Embedded val lyricChord: LyricChord,
    @Relation(
        parentColumn = "chord_root_id", // Clave foránea en LyricChord que referencia a ChordRoot
        entityColumn = "id", // Clave primaria de ChordRoot
    )
    val chordRoot: ChordRoot,
)
