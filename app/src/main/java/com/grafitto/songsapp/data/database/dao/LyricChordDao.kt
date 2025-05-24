package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.LyricChord
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricChordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lyricChord: LyricChord): Long

    @Update
    suspend fun update(lyricChord: LyricChord)

    @Delete
    suspend fun delete(lyricChord: LyricChord)

    @Query("SELECT * FROM lyric_chords WHERE id = :id")
    suspend fun getById(id: Long): LyricChord?

    @Query("SELECT * FROM lyric_chords")
    fun getAll(): Flow<List<LyricChord>>
}

