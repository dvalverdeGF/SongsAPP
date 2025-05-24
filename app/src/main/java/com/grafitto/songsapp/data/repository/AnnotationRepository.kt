package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.AnnotationDao
import com.grafitto.songsapp.data.database.entity.Annotation
import com.grafitto.songsapp.data.database.relation.AnnotationWithSymbol

class AnnotationRepository(
    private val annotationDao: AnnotationDao,
) {
    fun getAnnotationWithSymbolById(annotationId: Long): LiveData<AnnotationWithSymbol> =
        annotationDao.getAnnotationWithSymbolById(annotationId)

    fun getAnnotationsByVerseId(verseId: Long): LiveData<List<Annotation>> = annotationDao.getAnnotationsByVerseId(verseId)

    suspend fun insert(annotation: Annotation) {
        annotationDao.insert(annotation)
    }

    suspend fun update(annotation: Annotation) {
        annotationDao.update(annotation)
    }

    suspend fun delete(annotation: Annotation) {
        annotationDao.delete(annotation)
    }
}
