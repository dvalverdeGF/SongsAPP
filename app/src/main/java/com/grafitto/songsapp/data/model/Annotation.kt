package com.grafitto.songsapp.data.model

// Asumiendo que AnnotationSymbol también será una clase de modelo
data class Annotation(
    val id: Int = 0,
    val text: String,
    val relativeCharPosition: Float,
    val pixelOffset: Float? = null,
    val annotationSymbol: AnnotationSymbol? = null, // o annotationSymbolId: Int
)
