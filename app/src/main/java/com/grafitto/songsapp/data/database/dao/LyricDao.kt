package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.Lyric
import com.grafitto.songsapp.data.database.relation.LyricWithAllDetails
import com.grafitto.songsapp.data.database.relation.LyricWithVerses
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

    @Transaction
    @Query("SELECT * FROM lyrics WHERE id = :lyricId")
    fun getLyricWithVersesById(lyricId: Long): LiveData<LyricWithVerses>

    @Transaction
    @Query("SELECT * FROM lyrics WHERE id = :lyricId")
    fun getLyricWithAllDetailsById(lyricId: Long): LiveData<LyricWithAllDetails>
}
