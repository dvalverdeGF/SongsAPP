package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.LyricChordDao
import com.grafitto.songsapp.data.database.entity.LyricChord
import com.grafitto.songsapp.data.database.relation.DetailedLyricChord
import com.grafitto.songsapp.data.database.relation.LyricChordWithChordRoot

class LyricChordRepository(
    private val lyricChordDao: LyricChordDao,
) {
    fun getLyricChordWithChordRootById(lyricChordId: Long): LiveData<LyricChordWithChordRoot> =
        lyricChordDao.getLyricChordWithChordRootById(lyricChordId)

    fun getDetailedLyricChordById(lyricChordId: Long): LiveData<DetailedLyricChord> = lyricChordDao.getDetailedLyricChordById(lyricChordId)

    fun getLyricChordsByVerseId(verseId: Long): LiveData<List<LyricChord>> = lyricChordDao.getLyricChordsByVerseId(verseId)

    suspend fun insert(lyricChord: LyricChord) {
        lyricChordDao.insert(lyricChord)
    }

    suspend fun update(lyricChord: LyricChord) {
        lyricChordDao.update(lyricChord)
    }

    suspend fun delete(lyricChord: LyricChord) {
        lyricChordDao.delete(lyricChord)
    }
}
