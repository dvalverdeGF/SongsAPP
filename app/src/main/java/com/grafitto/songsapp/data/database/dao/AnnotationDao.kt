package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.Annotation
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnotationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(annotation: Annotation): Long

    @Update
    suspend fun update(annotation: Annotation)

    @Delete
    suspend fun delete(annotation: Annotation)

    @Query("SELECT * FROM annotations WHERE id = :id")
    suspend fun getById(id: Long): Annotation?

    @Query("SELECT * FROM annotations")
    fun getAll(): Flow<List<Annotation>>
}
