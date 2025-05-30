package com.grafitto.songsapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.grafitto.songsapp.data.database.dao.CategoryDao
import com.grafitto.songsapp.data.database.entity.Category
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryDao: CategoryDao,
) : ViewModel() {
    val categories: LiveData<List<Category>> = categoryDao.getAllCategories()
    val songsByCategoryCount: LiveData<Map<Long, Int>> = categoryDao.getSongsCountByCategory()

    @Suppress("ktlint:standard:backing-property-naming")
    // Channel and Flow for save requests
    private val _saveRequestChannel = Channel<Unit>(Channel.BUFFERED)
    val saveRequestFlow = _saveRequestChannel.receiveAsFlow()

    fun requestSave() {
        viewModelScope.launch {
            _saveRequestChannel.send(Unit)
        }
    }

    fun saveCategory(
        name: String,
        currentCategoryToEdit: Category?,
        currentParentId: Long?,
    ): Boolean {
        if (name.isNotBlank()) {
            val category =
                if (currentCategoryToEdit != null) {
                    currentCategoryToEdit.copy(name = name, parentId = currentParentId)
                } else {
                    Category(name = name, parentId = currentParentId)
                }
            if (currentCategoryToEdit != null) {
                updateCategory(category)
            } else {
                insertCategory(category)
            }
            return true
        }
        return false
    }

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

    fun getCategoryPath(
        category: Category,
        allCategories: List<Category>,
    ): String {
        val path = mutableListOf<String>()
        var current: Category? = category
        while (current != null) {
            path.add(0, current.name ?: "(Sin nombre)")
            if (current.parentId == null) break
            current = allCategories.find { it.id == current.parentId }
            if (path.size > 5) { // Evitar rutas excesivamente largas o ciclos
                path.add(0, "...")
                break
            }
        }
        return path.joinToString(" > ")
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
