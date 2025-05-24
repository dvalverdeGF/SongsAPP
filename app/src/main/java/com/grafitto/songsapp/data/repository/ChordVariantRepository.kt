package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.ChordVariantDao
import com.grafitto.songsapp.data.database.entity.ChordVariant

class ChordVariantRepository(
    private val chordVariantDao: ChordVariantDao,
) {
    val allChordVariants: LiveData<List<ChordVariant>> = chordVariantDao.getAllChordVariants()

    suspend fun insert(chordVariant: ChordVariant) {
        chordVariantDao.insert(chordVariant)
    }

    // Podrías añadir aquí métodos para getById, update, delete si fueran necesarios
    // para esta entidad de catálogo.
}
