package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.model.Category
import com.grafitto.songsapp.data.model.Song
import kotlinx.coroutines.flow.Flow

interface SongsRepository {
    fun getAllSongs(): Flow<List<Song>>

    suspend fun getSongById(id: Int): Song?

    suspend fun addSong(song: Song)

    suspend fun updateSong(song: Song)

    suspend fun clearSongs()

    suspend fun getAllCategories(): List<Category>

    suspend fun getCategoryById(categoryId: Int): Category?

    suspend fun addCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategory(categoryId: Int)

    suspend fun getCategoryWithChildren(categoryId: Int): Category?

    suspend fun getCategoryTree(): List<Category>

    suspend fun getRootCategories(): List<Category>
}
