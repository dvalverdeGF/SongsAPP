package com.grafitto.songsapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grafitto.songsapp.data.database.entity.VerseEntity

@Dao
interface VerseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VerseEntity>)

    @Query("SELECT * FROM verses WHERE songId = :songId ORDER BY position")
    fun getVersesBySongId(songId: Int): List<VerseEntity>

    @Query("DELETE FROM verses WHERE songId = :songId")
    suspend fun deleteVersesBySongId(songId: Int)

    @Query("DELETE FROM verses")
    suspend fun deleteAllVerses()
}
