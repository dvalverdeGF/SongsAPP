package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.Lyric
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lyric: Lyric): Long

    @Update
    suspend fun update(lyric: Lyric)

    @Delete
    suspend fun delete(lyric: Lyric)

    @Query("SELECT * FROM lyrics WHERE id = :id")
    suspend fun getById(id: Long): Lyric?

    @Query("SELECT * FROM lyrics")
    fun getAll(): Flow<List<Lyric>>
}

