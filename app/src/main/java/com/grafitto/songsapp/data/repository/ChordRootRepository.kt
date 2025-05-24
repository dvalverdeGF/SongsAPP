package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.ChordRootDao
import com.grafitto.songsapp.data.database.entity.ChordRoot

class ChordRootRepository(
    private val chordRootDao: ChordRootDao,
) {
    val allChordRoots: LiveData<List<ChordRoot>> = chordRootDao.getAllChordRoots()

    fun getChordRootById(chordRootId: Long): LiveData<ChordRoot> = chordRootDao.getChordRootById(chordRootId)

    suspend fun insert(chordRoot: ChordRoot) {
        chordRootDao.insert(chordRoot)
    }

    suspend fun update(chordRoot: ChordRoot) {
        chordRootDao.update(chordRoot)
    }

    suspend fun delete(chordRoot: ChordRoot) {
        chordRootDao.delete(chordRoot)
    }
}
