package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.ChordModifierDao
import com.grafitto.songsapp.data.database.entity.ChordModifier

class ChordModifierRepository(
    private val chordModifierDao: ChordModifierDao,
) {
    val allChordModifiers: LiveData<List<ChordModifier>> = chordModifierDao.getAllChordModifiers()

    suspend fun insert(chordModifier: ChordModifier) {
        chordModifierDao.insert(chordModifier)
    }

    // Podrías añadir aquí métodos para getById, update, delete si fueran necesarios
    // para esta entidad de catálogo.
}
