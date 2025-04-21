package com.grafitto.songsapp.data.database.dao

// SongDao.kt
@Dao
interface SongDao {
    @Query("SELECT * FROM songs ORDER BY title ASC")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Transaction
    @Query("SELECT * FROM songs WHERE id = :songId")
    suspend fun getSongWithVerses(songId: Int): SongWithVerses?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSong(song: SongEntity): Long

    @Update
    suspend fun updateSong(song: SongEntity)
}

// VerseDao.kt
@Dao
interface VerseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerses(verses: List<VerseEntity>)

    @Query("DELETE FROM verses WHERE songId = :songId")
    suspend fun deleteVersesBySongId(songId: Int)
}
