package com.grafitto.songsapp.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.grafitto.songsapp.data.database.entity.Verse

// Esta clase representa un Verso con todos sus detalles asociados:
// - Acordes detallados (DetailedLyricChord ya incluye LyricChord y sus componentes como ChordRoot, Accidental, etc.)
// - Anotaciones con sus símbolos (AnnotationWithSymbol ya incluye Annotation y su AnnotationSymbol)
data class VerseWithAllDetails(
    @Embedded val verse: Verse,
    // Relación para obtener la lista de acordes detallados para este verso.
    // Room buscará en la entidad LyricChord (embebida dentro de DetailedLyricChord)
    // una columna llamada "verse_id" para hacer la correspondencia con el "id" de Verse.
    @Relation(
        entity = DetailedLyricChord::class,
        parentColumn = "id", // Corresponde a Verse.id
        entityColumn = "verse_id", // Corresponde a LyricChord.verseId (asumiendo que LyricChord está en DetailedLyricChord y tiene verseId)
    )
    val detailedLyricChords: List<DetailedLyricChord>,
    // Relación para obtener la lista de anotaciones con sus símbolos para este verso.
    // Room buscará en la entidad Annotation (embebida dentro de AnnotationWithSymbol)
    // una columna llamada "verse_id" para hacer la correspondencia con el "id" de Verse.
    @Relation(
        entity = AnnotationWithSymbol::class,
        parentColumn = "id", // Corresponde a Verse.id
        entityColumn = "verse_id", // Corresponde a Annotation.verseId (asumiendo que Annotation está en AnnotationWithSymbol y tiene verseId)
    )
    val annotationsWithSymbols: List<AnnotationWithSymbol>,
)
