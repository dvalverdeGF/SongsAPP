package com.grafitto.songsapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.grafitto.songsapp.data.database.entity.Song
import com.grafitto.songsapp.data.database.relation.SongWithVerses
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY name ASC")
    fun getAllSongs(): Flow<List<Song>>

    @Transaction
    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongWithVerses(songId: Int): SongWithVerses?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: Song): Long

    @Update
    suspend fun updateSong(song: Song)

    @Query("DELETE FROM songs")
    suspend fun deleteAllSongs()
}
