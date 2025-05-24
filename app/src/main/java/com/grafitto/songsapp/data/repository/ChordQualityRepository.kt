package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.ChordQualityDao
import com.grafitto.songsapp.data.database.entity.ChordQuality

class ChordQualityRepository(
    private val chordQualityDao: ChordQualityDao,
) {
    val allChordQualities: LiveData<List<ChordQuality>> = chordQualityDao.getAllChordQualities()

    suspend fun insert(chordQuality: ChordQuality) {
        chordQualityDao.insert(chordQuality)
    }
}
