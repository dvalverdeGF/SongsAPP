package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Song
import com.grafitto.songsapp.data.database.entity.Verse

data class SongWithVerses(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "id", // Campo 'id' en la entidad Song
        entityColumn = "song_id", // Campo 'song_id' en la entidad Verse que referencia a Song
    )
    val verses: List<Verse>,
)
