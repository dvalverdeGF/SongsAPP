package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Lyric
import com.grafitto.songsapp.data.database.entity.Song

data class SongWithLyric(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "id", // ID de Song
        entityColumn = "song_id", // Columna en Lyric que referencia a Song
    )
    val lyric: Lyric?, // Puede ser null si una canción no tiene letra o la relación es opcional
)
