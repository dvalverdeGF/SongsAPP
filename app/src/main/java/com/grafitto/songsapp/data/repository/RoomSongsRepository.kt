package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.database.dao.CategoryDao
import com.grafitto.songsapp.data.database.dao.SongDao
import com.grafitto.songsapp.data.database.dao.VerseDao
import com.grafitto.songsapp.data.database.entity.CategoryEntity
import com.grafitto.songsapp.data.database.entity.SongEntity
import com.grafitto.songsapp.data.database.entity.VerseEntity
import com.grafitto.songsapp.data.database.relation.SongWithVerses
import com.grafitto.songsapp.data.model.Category
import com.grafitto.songsapp.data.model.Song
import com.grafitto.songsapp.data.model.Verse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomSongsRepository(
    private val songDao: SongDao,
    private val verseDao: VerseDao,
    private val categoryDao: CategoryDao,
) : SongsRepository {
    override fun getAllSongs(): Flow<List<Song>> =
        songDao.getAllSongs().map { entities ->
            entities.map { entity ->
                Song(entity.id, entity.title, entity.artist)
            }
        }

    override suspend fun getSongById(id: Int): Song? {
        val songWithVerses = songDao.getSongWithVerses(id) ?: return null
        return songWithVerses.toModel()
    }

    override suspend fun addSong(song: Song) {
        val songId = songDao.insertSong(song.toEntity()).toInt()

        val versesWithSongId =
            song.verses.mapIndexed { index, verse ->
                verse.toEntity(songId, index)
            }
        verseDao.insertVerses(versesWithSongId)
    }

    override suspend fun updateSong(song: Song) {
        songDao.updateSong(song.toEntity())
        verseDao.deleteVersesBySongId(song.id)

        val versesWithSongId =
            song.verses.mapIndexed { index, verse ->
                verse.toEntity(song.id, index)
            }
        verseDao.insertVerses(versesWithSongId)
    }

    override suspend fun clearSongs() {
        songDao.deleteAllSongs()
        verseDao.deleteAllVerses()
    }

    // Implementación de métodos para categorías
    override suspend fun getAllCategories(): List<Category> = categoryDao.getAllCategories().map { it.toCategory() }

    override suspend fun getCategoryById(categoryId: Int): Category? = categoryDao.getCategoryById(categoryId)?.toCategory()

    override suspend fun addCategory(category: Category) {
        val categoryEntity =
            CategoryEntity(
                id = 0,
                name = category.name,
                description = category.description,
                parentId = category.parentId,
            )
        categoryDao.insertCategory(categoryEntity)
    }

    override suspend fun updateCategory(category: Category) {
        val categoryEntity =
            CategoryEntity(
                id = category.id,
                name = category.name,
                description = category.description,
                parentId = category.parentId,
            )
        categoryDao.updateCategory(categoryEntity)
    }

    override suspend fun deleteCategory(categoryId: Int) {
        categoryDao.deleteCategoryById(categoryId)
    }

    override suspend fun getCategoryWithChildren(categoryId: Int): Category? {
        val category = categoryDao.getCategoryById(categoryId)?.toCategory() ?: return null
        val children = buildCategoryTree(getAllCategories(), categoryId)
        return category.copy(children = children)
    }

    override suspend fun getCategoryTree(): List<Category> {
        val allCategories = categoryDao.getAllCategories().map { it.toCategory() }
        return buildCategoryTree(allCategories)
    }

    override suspend fun getRootCategories(): List<Category> = categoryDao.getRootCategories().map { it.toCategory() }

    // Métodos de conversión
    private fun SongEntity.toModel(): Song = Song(id, title, artist)

    private fun Song.toEntity(): SongEntity = SongEntity(id, title, artist)

    private fun Verse.toEntity(
        songId: Int,
        position: Int,
    ): VerseEntity = VerseEntity(0, songId, chords, lyrics, position)

    private fun SongWithVerses.toModel(): Song =
        Song(
            song.id,
            song.title,
            song.artist,
            verses.map { Verse(it.chords, it.lyrics) }.toString(),
        )

    private fun CategoryEntity.toCategory(): Category = Category(id, name, description, parentId)

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
