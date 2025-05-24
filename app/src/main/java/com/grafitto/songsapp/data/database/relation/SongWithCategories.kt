package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.data.database.entity.Song
import com.grafitto.songsapp.data.database.entity.SongCategory

data class SongWithCategories(
    @Embedded val song: Song,
    @Relation(
        parentColumn = "id", // ID de Song
        entityColumn = "id", // ID de Category
        associateBy =
            Junction(
                value = SongCategory::class,
                parentColumn = "song_id", // Columna en SongCategory que referencia a Song
                entityColumn = "category_id", // Columna en SongCategory que referencia a Category
            ),
    )
    val categories: List<Category>,
)
