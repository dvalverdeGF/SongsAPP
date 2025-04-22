package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.model.Category
import com.grafitto.songsapp.data.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemorySongsRepository : SongsRepository {
    @Suppress("ktlint:standard:backing-property-naming")
    private val _songs = mutableListOf<Song>()
    private val _songsFlow = MutableStateFlow<List<Song>>(_songs.toList())

    override fun getAllSongs(): Flow<List<Song>> = _songsFlow.asStateFlow()

    override suspend fun getSongById(id: Int): Song? = _songs.find { it.id == id }

    override suspend fun addSong(song: Song) {
        val newId = (_songs.maxOfOrNull { it.id } ?: 0) + 1
        _songs.add(song.copy(id = newId))
        _songsFlow.value = _songs.toList()
    }

    override suspend fun updateSong(updatedSong: Song) {
        val index = _songs.indexOfFirst { it.id == updatedSong.id }
        if (index != -1) {
            _songs[index] = updatedSong
            _songsFlow.value = _songs.toList()
        }
    }

    override suspend fun clearSongs() {
        _songs.clear()
        _songsFlow.value = emptyList()
    }

    private val categories = mutableListOf<Category>()

    override suspend fun getAllCategories(): List<Category> = categories

    override suspend fun getCategoryById(categoryId: Int): Category? = categories.find { it.id == categoryId }

    override suspend fun addCategory(category: Category) {
        categories.add(category)
    }

    override suspend fun updateCategory(category: Category) {
        val index = categories.indexOfFirst { it.id == category.id }
        if (index >= 0) {
            categories[index] = category
        }
    }

    override suspend fun deleteCategory(categoryId: Int) {
        categories.removeIf { it.id == categoryId }
    }

    override suspend fun getCategoryWithChildren(categoryId: Int): Category? {
        val category = categories.find { it.id == categoryId } ?: return null
        val children = buildCategoryTree(categories, categoryId)
        return category.copy(children = children)
    }

    override suspend fun getCategoryTree(): List<Category> = buildCategoryTree(categories)

    override suspend fun getRootCategories(): List<Category> = categories.filter { it.parentId == null }

    private fun buildCategoryTree(
        categories: List<Category>,
        parentId: Int? = null,
    ): List<Category> =
        categories
            .filter { it.parentId == parentId }
            .map { category ->
                category.copy(
                    children = buildCategoryTree(categories, category.id),
                )
            }
}
