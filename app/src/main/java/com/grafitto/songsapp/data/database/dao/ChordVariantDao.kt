package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.ChordVariant

@Dao
interface ChordVariantDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chordVariant: ChordVariant)

    @Update
    suspend fun update(chordVariant: ChordVariant)

    @Delete
    suspend fun delete(chordVariant: ChordVariant)

    @Query("SELECT * FROM chord_variants WHERE id = :id")
    suspend fun getById(id: Long): ChordVariant?

    @Query("SELECT * FROM chord_variants")
    fun getAllChordVariants(): LiveData<List<ChordVariant>>
}
