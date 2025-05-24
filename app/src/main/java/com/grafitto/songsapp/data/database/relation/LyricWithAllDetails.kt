package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Lyric

// Esta clase representa una Letra (Lyric) con todos sus detalles asociados:
// - Una lista de Versos, donde cada verso incluye todos sus detalles (acordes y anotaciones).
data class LyricWithAllDetails(
    @Embedded val lyric: Lyric,
    // Relación para obtener la lista de versos detallados para esta letra.
    // Room buscará en la entidad Verse (embebida dentro de VerseWithAllDetails)
    // una columna llamada "lyric_id" para hacer la correspondencia con el "id" de Lyric.
    @Relation(
        entity = VerseWithAllDetails::class, // La clase que definimos para los detalles del verso
        parentColumn = "id", // Corresponde a Lyric.id
        entityColumn = "lyric_id", // Corresponde a Verse.lyricId (asumiendo que Verse está en VerseWithAllDetails y tiene lyricId)
    )
    val versesWithAllDetails: List<VerseWithAllDetails>,
)
