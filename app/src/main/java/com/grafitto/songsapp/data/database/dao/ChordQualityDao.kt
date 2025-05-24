package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.ChordQuality
import kotlinx.coroutines.flow.Flow

@Dao
interface ChordQualityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chordQuality: ChordQuality): Long

    @Update
    suspend fun update(chordQuality: ChordQuality)

    @Delete
    suspend fun delete(chordQuality: ChordQuality)

    @Query("SELECT * FROM chord_qualities WHERE id = :id")
    suspend fun getById(id: Long): ChordQuality?

    @Query("SELECT * FROM chord_qualities")
    fun getAll(): Flow<List<ChordQuality>>
}

