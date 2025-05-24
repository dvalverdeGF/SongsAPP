package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Author // Asegúrate de que Author.kt exista
import com.grafitto.songsapp.data.database.entity.Song

data class AuthorWithSongs(
    @Embedded val author: Author,
    @Relation(
        parentColumn = "id", // Clave primaria de Author
        entityColumn = "author_id", // Clave foránea en Song que referencia a Author
    )
    val songs: List<Song>,
)
