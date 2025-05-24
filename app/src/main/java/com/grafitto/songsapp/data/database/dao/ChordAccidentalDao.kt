package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.ChordAccidental
import kotlinx.coroutines.flow.Flow

@Dao
interface ChordAccidentalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chordAccidental: ChordAccidental): Long

    @Update
    suspend fun update(chordAccidental: ChordAccidental)

    @Delete
    suspend fun delete(chordAccidental: ChordAccidental)

    @Query("SELECT * FROM chord_accidentals WHERE id = :id")
    suspend fun getById(id: Long): ChordAccidental?

    @Query("SELECT * FROM chord_accidentals")
    fun getAll(): Flow<List<ChordAccidental>>
}

