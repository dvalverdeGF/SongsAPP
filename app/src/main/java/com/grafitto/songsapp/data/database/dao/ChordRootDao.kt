package com.grafitto.songsapp.data.database.dao

import androidx.room.*
import com.grafitto.songsapp.data.database.entity.ChordRoot
import kotlinx.coroutines.flow.Flow

@Dao
interface ChordRootDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chordRoot: ChordRoot): Long

    @Update
    suspend fun update(chordRoot: ChordRoot)

    @Delete
    suspend fun delete(chordRoot: ChordRoot)

    @Query("SELECT * FROM chord_roots WHERE id = :id")
    suspend fun getById(id: Long): ChordRoot?

    @Query("SELECT * FROM chord_roots")
    fun getAll(): Flow<List<ChordRoot>>
}

