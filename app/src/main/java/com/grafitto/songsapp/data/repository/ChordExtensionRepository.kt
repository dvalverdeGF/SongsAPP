package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.ChordExtensionDao
import com.grafitto.songsapp.data.database.entity.ChordExtension

class ChordExtensionRepository(
    private val chordExtensionDao: ChordExtensionDao,
) {
    val allChordExtensions: LiveData<List<ChordExtension>> = chordExtensionDao.getAllChordExtensions()

    suspend fun insert(chordExtension: ChordExtension) {
        chordExtensionDao.insert(chordExtension)
    }

    // Podrías añadir aquí métodos para getById, update, delete si fueran necesarios
    // para esta entidad de catálogo.
}
