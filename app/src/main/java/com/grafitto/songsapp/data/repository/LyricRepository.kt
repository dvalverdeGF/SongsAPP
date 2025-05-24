package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.LyricDao
import com.grafitto.songsapp.data.database.entity.Lyric
import com.grafitto.songsapp.data.database.relation.LyricWithAllDetails
import com.grafitto.songsapp.data.database.relation.LyricWithVerses

class LyricRepository(
    private val lyricDao: LyricDao,
) {
    fun getLyricWithVersesById(lyricId: Long): LiveData<LyricWithVerses> = lyricDao.getLyricWithVersesById(lyricId)

    fun getLyricWithAllDetailsById(lyricId: Long): LiveData<LyricWithAllDetails> = lyricDao.getLyricWithAllDetailsById(lyricId)

    suspend fun insert(lyric: Lyric) {
        lyricDao.insert(lyric)
    }

    suspend fun update(lyric: Lyric) {
        lyricDao.update(lyric)
    }

    suspend fun delete(lyric: Lyric) {
        lyricDao.delete(lyric)
    }
}
