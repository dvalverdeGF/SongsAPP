package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.ChordAccidental // Asegúrate de que ChordAccidental.kt exista
import com.grafitto.songsapp.data.database.entity.ChordExtension // Asegúrate de que ChordExtension.kt exista
import com.grafitto.songsapp.data.database.entity.ChordModifier // Asegúrate de que ChordModifier.kt exista
import com.grafitto.songsapp.data.database.entity.ChordQuality // Asegúrate de que ChordQuality.kt exista
import com.grafitto.songsapp.data.database.entity.ChordRoot // Asegúrate de que ChordRoot.kt exista
import com.grafitto.songsapp.data.database.entity.ChordVariant // Asegúrate de que ChordVariant.kt exista
import com.grafitto.songsapp.data.database.entity.LyricChord // Asegúrate de que LyricChord.kt tenga las FKs necesarias

data class DetailedLyricChord(
    @Embedded val lyricChord: LyricChord, // Sin prefix
    @Relation(
        parentColumn = "chord_root_id",
        entityColumn = "id",
    )
    val chordRoot: ChordRoot,
    @Relation(
        parentColumn = "accidental_id",
        entityColumn = "id",
    )
    val accidental: ChordAccidental?,
    @Relation(
        parentColumn = "quality_id",
        entityColumn = "id",
    )
    val quality: ChordQuality?,
    @Relation(
        parentColumn = "extension_id",
        entityColumn = "id",
    )
    val extension: ChordExtension?,
    @Relation(
        parentColumn = "modifier_id",
        entityColumn = "id",
    )
    val modifier: ChordModifier?,
    @Relation(
        parentColumn = "variant_id",
        entityColumn = "id",
    )
    val variant: ChordVariant?,
)
