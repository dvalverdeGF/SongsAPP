package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.VerseDao
import com.grafitto.songsapp.data.database.entity.Verse
import com.grafitto.songsapp.data.database.relation.VerseWithAllDetails
import com.grafitto.songsapp.data.database.relation.VerseWithAnnotations
import com.grafitto.songsapp.data.database.relation.VerseWithLyricChords

class VerseRepository(
    private val verseDao: VerseDao,
) {
    fun getVerseWithLyricChordsById(verseId: Long): LiveData<VerseWithLyricChords> = verseDao.getVerseWithLyricChordsById(verseId)

    fun getVerseWithAnnotationsById(verseId: Long): LiveData<VerseWithAnnotations> = verseDao.getVerseWithAnnotationsById(verseId)

    fun getVerseWithAllDetailsById(verseId: Long): LiveData<VerseWithAllDetails> = verseDao.getVerseWithAllDetailsById(verseId)

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
