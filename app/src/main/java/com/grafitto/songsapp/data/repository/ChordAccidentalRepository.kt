package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.ChordAccidentalDao
import com.grafitto.songsapp.data.database.entity.ChordAccidental

class ChordAccidentalRepository(
    private val chordAccidentalDao: ChordAccidentalDao,
) {
    val allChordAccidentals: LiveData<List<ChordAccidental>> = chordAccidentalDao.getAllChordAccidentals()

    suspend fun insert(chordAccidental: ChordAccidental) {
        chordAccidentalDao.insert(chordAccidental)
    }
}
