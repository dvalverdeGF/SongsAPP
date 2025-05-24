package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.SongCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SongCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(songCategory: SongCategory): Long

    @Update
    suspend fun update(songCategory: SongCategory)

    @Delete
    suspend fun delete(songCategory: SongCategory)

    @Query("SELECT * FROM song_categories WHERE songId = :songId AND categoryId = :categoryId")
    suspend fun getById(songId: Long, categoryId: Long): SongCategory?

    @Query("SELECT * FROM song_categories")
    fun getAll(): Flow<List<SongCategory>>
}

