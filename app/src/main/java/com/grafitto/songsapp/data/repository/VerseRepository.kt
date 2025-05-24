package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.database.dao.VerseDao
import com.grafitto.songsapp.data.database.entity.Verse

class VerseRepository(
    private val verseDao: VerseDao,
) {
    suspend fun insert(verse: Verse) {
        verseDao.insert(verse)
    }

    suspend fun update(verse: Verse) {
        verseDao.update(verse)
    }

    suspend fun delete(verse: Verse) {
        verseDao.delete(verse)
    }
}
