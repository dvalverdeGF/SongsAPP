package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.ChordModifier

@Dao
interface ChordModifierDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chordModifier: ChordModifier)

    @Update
    suspend fun update(chordModifier: ChordModifier)

    @Delete
    suspend fun delete(chordModifier: ChordModifier)

    @Query("SELECT * FROM chord_modifiers WHERE id = :id")
    suspend fun getById(id: Long): ChordModifier?

    @Query("SELECT * FROM chord_modifiers")
    fun getAllChordModifiers(): LiveData<List<ChordModifier>>
}
