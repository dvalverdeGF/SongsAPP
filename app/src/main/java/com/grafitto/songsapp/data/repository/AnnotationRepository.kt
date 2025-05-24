package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.LyricAnnotationDao
import com.grafitto.songsapp.data.database.entity.LyricAnnotation
import com.grafitto.songsapp.data.database.relation.AnnotationWithSymbol

class AnnotationRepository(
    private val annotationDao: LyricAnnotationDao,
) {
    fun getAnnotationWithSymbolById(annotationId: Long): LiveData<AnnotationWithSymbol> =
        annotationDao.getAnnotationWithSymbolById(annotationId)

    fun getAnnotationsByVerseId(verseId: Long): LiveData<List<LyricAnnotation>> = annotationDao.getAnnotationsByVerseId(verseId)

    suspend fun insert(annotation: LyricAnnotation) {
        annotationDao.insert(annotation)
    }

    suspend fun update(annotation: LyricAnnotation) {
        annotationDao.update(annotation)
    }

    suspend fun delete(annotation: LyricAnnotation) {
        annotationDao.delete(annotation)
    }
}
