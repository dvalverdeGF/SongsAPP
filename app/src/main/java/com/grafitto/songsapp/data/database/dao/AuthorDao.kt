package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.grafitto.songsapp.data.database.entity.Author
import com.grafitto.songsapp.data.database.relation.AuthorWithSongs
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(author: Author): Long

    @Update
    suspend fun update(author: Author)

    @Delete
    suspend fun delete(author: Author)

    @Query("SELECT * FROM authors WHERE id = :id")
    suspend fun getById(id: Long): Author?

    @Query("SELECT * FROM authors")
    fun getAll(): Flow<List<Author>>

    @Query("SELECT * FROM authors")
    fun getAllAuthors(): LiveData<List<Author>>

    @Transaction
    @Query("SELECT * FROM authors WHERE id = :lng")
    fun getAuthorWithSongsById(lng: Long): LiveData<AuthorWithSongs>
}
