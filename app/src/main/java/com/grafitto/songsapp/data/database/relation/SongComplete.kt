package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Author
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.data.database.entity.Song
import com.grafitto.songsapp.data.database.entity.SongCategory

// Esta clase representa una Canción (Song) con una vista completa de sus datos relacionados:
// - El objeto Autor asociado.
// - Una lista de Categorías a las que pertenece.
// - La Letra completa con todos sus Versos, y para cada Verso, sus Acordes detallados y Anotaciones.
data class SongComplete(
    @Embedded val song: Song,
    // Relación para obtener el Autor de la canción.
    @Relation(
        entity = Author::class,
        parentColumn = "author_id", // Clave foránea en la tabla songs
        entityColumn = "id", // Clave primaria en la tabla authors
    )
    val author: Author?, // Puede ser nulo si la canción no tiene autor o la FK es opcional
    // Relación para obtener la lista de Categorías de la canción (relación Muchos a Muchos).
    // Utiliza la tabla de unión SongCategory.
    @Relation(
        parentColumn = "id", // Corresponde a Song.id
        entityColumn = "id", // Corresponde a Category.id
        associateBy =
            Junction(
                value = SongCategory::class,
                parentColumn = "song_id", // Columna en SongCategory que referencia a Song
                entityColumn = "category_id", // Columna en SongCategory que referencia a Category
            ),
    )
    val categories: List<Category>,
    // Relación para obtener la Letra detallada de la canción.
    // Room buscará en la entidad Lyric (embebida dentro de LyricWithAllDetails)
    // una columna llamada "song_id" para hacer la correspondencia con el "id" de Song.
    @Relation(
        entity = LyricWithAllDetails::class, // La clase que definimos para los detalles de la letra
        parentColumn = "id", // Corresponde a Song.id
        entityColumn = "song_id", // Corresponde a Lyric.songId (asumiendo que Lyric está en LyricWithAllDetails y tiene songId)
    )
    val lyricWithAllDetails: LyricWithAllDetails?, // Puede ser nulo si la canción no tiene letra
)
