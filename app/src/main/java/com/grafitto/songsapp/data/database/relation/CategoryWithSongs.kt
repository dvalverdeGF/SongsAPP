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
        parentColumn = "id", // Clave primaria en Category
        entityColumn = "id", // Clave primaria en Song
        associateBy =
            Junction(
                value = SongCategory::class,
                parentColumn = "category_id", // FK en SongCategory que referencia Category
                entityColumn = "song_id", // FK en SongCategory que referencia Song
            ),
    )
    val songs: List<Song>,
)
