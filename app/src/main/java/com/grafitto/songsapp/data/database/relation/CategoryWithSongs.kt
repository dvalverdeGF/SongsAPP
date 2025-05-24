package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Category // Asegúrate de que Category.kt exista
import com.grafitto.songsapp.data.database.entity.Song
import com.grafitto.songsapp.data.database.entity.SongCategory

data class CategoryWithSongs(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id", // Clave primaria de Category
        entityColumn = "id", // Clave primaria de Song
        associateBy =
            Junction(
                value = SongCategory::class,
                parentColumn = "category_id", // Clave foránea en SongCategory que referencia a Category
                entityColumn = "song_id", // Clave foránea en SongCategory que referencia a Song
            ),
    )
    val songs: List<Song>,
)
