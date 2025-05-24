package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.AnnotationSymbolDao
import com.grafitto.songsapp.data.database.entity.AnnotationSymbol

class AnnotationSymbolRepository(
    private val annotationSymbolDao: AnnotationSymbolDao,
) {
    val allAnnotationSymbols: LiveData<List<AnnotationSymbol>> = annotationSymbolDao.getAllAnnotationSymbols()

    fun getAnnotationSymbolById(annotationSymbolId: Long): LiveData<AnnotationSymbol> =
        annotationSymbolDao.getAnnotationSymbolById(annotationSymbolId)

    suspend fun insert(annotationSymbol: AnnotationSymbol) {
        annotationSymbolDao.insert(annotationSymbol)
    }

    suspend fun update(annotationSymbol: AnnotationSymbol) {
        annotationSymbolDao.update(annotationSymbol)
    }

    suspend fun delete(annotationSymbol: AnnotationSymbol) {
        annotationSymbolDao.delete(annotationSymbol)
    }
}
