package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.CategoryDao
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.data.database.relation.CategoryWithSongs

class CategoryRepository(
    private val categoryDao: CategoryDao,
) {
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    fun getCategoryWithSongsById(categoryId: Long): LiveData<CategoryWithSongs> = categoryDao.getCategoryWithSongsById(categoryId)

    suspend fun insert(category: Category) {
        categoryDao.insertCategory(category)
    }

    suspend fun update(category: Category) {
        categoryDao.updateCategory(category)
    }

    suspend fun delete(category: Category) {
        categoryDao.deleteCategory(category)
    }
}
