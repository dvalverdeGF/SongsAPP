package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.LyricChord
import com.grafitto.songsapp.data.database.relation.DetailedLyricChord
import com.grafitto.songsapp.data.database.relation.LyricChordWithChordRoot
import kotlinx.coroutines.flow.Flow

@Dao
interface LyricChordDao {
    @Transaction
    @Query("SELECT * FROM lyric_chords WHERE id = :lyricChordId")
    fun getLyricChordWithChordRootById(lyricChordId: Long): LiveData<LyricChordWithChordRoot>

    @Transaction
    @Query("SELECT * FROM lyric_chords WHERE id = :lyricChordId")
    fun getDetailedLyricChordById(lyricChordId: Long): LiveData<DetailedLyricChord>

    @Query("SELECT * FROM lyric_chords WHERE verse_id = :verseId")
    fun getLyricChordsByVerseId(verseId: Long): LiveData<List<LyricChord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(lyricChord: LyricChord)

    @Update
    suspend fun update(lyricChord: LyricChord)

    @Delete
    suspend fun delete(lyricChord: LyricChord)

    @Query("SELECT * FROM lyric_chords WHERE id = :id")
    suspend fun getById(id: Long): LyricChord?

    @Query("SELECT * FROM lyric_chords")
    fun getAll(): Flow<List<LyricChord>>
}
