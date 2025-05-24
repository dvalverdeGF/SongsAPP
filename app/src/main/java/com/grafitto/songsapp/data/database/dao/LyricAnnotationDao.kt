package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.LyricAnnotation
import com.grafitto.songsapp.data.database.relation.AnnotationWithSymbol
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricAnnotationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(annotation: LyricAnnotation): Long

    @Update
    suspend fun update(annotation: LyricAnnotation)

    @Delete
    suspend fun delete(annotation: LyricAnnotation)

    @Query("SELECT * FROM lyric_annotations WHERE id = :id")
    suspend fun getById(id: Long): LyricAnnotation?

    @Query("SELECT * FROM lyric_annotations")
    fun getAll(): Flow<List<LyricAnnotation>>

    @Query("SELECT * FROM lyric_annotations WHERE verse_id = :verseId")
    fun getAnnotationsByVerseId(verseId: Long): LiveData<List<LyricAnnotation>>

    @Transaction
    @Query("SELECT * FROM lyric_annotations WHERE id = :id")
    fun getAnnotationWithSymbolById(id: Long): LiveData<AnnotationWithSymbol>
}
