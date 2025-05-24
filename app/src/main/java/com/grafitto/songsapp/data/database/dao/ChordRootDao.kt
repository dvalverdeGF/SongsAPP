package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.ChordRoot

@Dao
interface ChordRootDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chordRoot: ChordRoot): Long

    @Update
    suspend fun update(chordRoot: ChordRoot)

    @Delete
    suspend fun delete(chordRoot: ChordRoot)

    @Query("SELECT * FROM chord_roots WHERE id = :id")
    fun getChordRootById(id: Long): LiveData<ChordRoot>

    @Query("SELECT * FROM chord_roots")
    fun getAllChordRoots(): LiveData<List<ChordRoot>>
}
