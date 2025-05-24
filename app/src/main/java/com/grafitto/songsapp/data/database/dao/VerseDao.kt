package com.grafitto.songsapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grafitto.songsapp.data.database.entity.Verse

@Dao
interface VerseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<Verse>)

    @Query("SELECT * FROM verses WHERE song_id = :songId ORDER BY order_in_lyric ASC")
    fun getVersesBySongId(songId: Int): List<Verse>

    @Query("DELETE FROM verses WHERE song_id = :songId")
    suspend fun deleteVersesBySongId(songId: Int)

    @Query("DELETE FROM verses")
    suspend fun deleteAllVerses()
}
