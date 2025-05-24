package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.AnnotationSymbol
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnotationSymbolDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(annotationSymbol: AnnotationSymbol): Long

    @Update
    suspend fun update(annotationSymbol: AnnotationSymbol)

    @Delete
    suspend fun delete(annotationSymbol: AnnotationSymbol)

    @Query("SELECT * FROM annotation_symbols WHERE id = :id")
    suspend fun getById(id: Long): AnnotationSymbol?

    @Query("SELECT * FROM annotation_symbols")
    fun getAll(): Flow<List<AnnotationSymbol>>
}
