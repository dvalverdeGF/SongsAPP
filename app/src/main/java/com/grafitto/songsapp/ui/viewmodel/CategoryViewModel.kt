package com.grafitto.songsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grafitto.songsapp.data.database.dao.CategoryDao
import com.grafitto.songsapp.data.database.entity.Category
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryDao: CategoryDao,
) : ViewModel() {
    val categories: LiveData<List<Category>> = categoryDao.getAllCategories()

    fun insertCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.insertCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryDao.deleteCategory(category)
        }
    }
}

class CategoryViewModelFactory(
    private val categoryDao: CategoryDao,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoryViewModel(categoryDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
