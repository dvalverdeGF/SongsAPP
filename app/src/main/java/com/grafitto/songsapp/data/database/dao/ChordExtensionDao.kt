package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.ChordExtension

@Dao
interface ChordExtensionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chordExtension: ChordExtension)

    @Update
    suspend fun update(chordExtension: ChordExtension)

    @Delete
    suspend fun delete(chordExtension: ChordExtension)

    @Query("SELECT * FROM chord_extensions WHERE id = :id")
    suspend fun getById(id: Long): ChordExtension?

    @Query("SELECT * FROM chord_extensions")
    fun getAllChordExtensions(): LiveData<List<ChordExtension>>
}
