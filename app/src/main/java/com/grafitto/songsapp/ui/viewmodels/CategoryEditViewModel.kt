// app/src/main/java/com/grafitto/songsapp/ui/viewmodels/CategoryEditViewModel.kt
package com.grafitto.songsapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grafitto.songsapp.data.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryEditViewModel(
    private val repository: SongsRepository,
) : ViewModel() {
    private val _category = MutableStateFlow<Category?>(null)
    val category: StateFlow<Category?> = _category.asStateFlow()

    private val _availableParentCategories = MutableStateFlow<List<Category>>(emptyList())
    val availableParentCategories: StateFlow<List<Category>> = _availableParentCategories.asStateFlow()

    fun loadCategory(categoryId: Int) {
        if (categoryId <= 0) return

        viewModelScope.launch {
            val loadedCategory = repository.getCategoryById(categoryId)
            _category.value = loadedCategory
        }
    }

    fun loadAvailableParentCategories(currentCategoryId: Int) {
        viewModelScope.launch {
            // Obtener todas las categorías y filtrar para evitar la categoría actual
            val allCategories = repository.getAllCategories()
            val filteredCategories =
                if (currentCategoryId > 0) {
                    // Filtrar la categoría actual y sus descendientes para evitar ciclos
                    allCategories.filter { it.id != currentCategoryId }
                } else {
                    allCategories
                }
            _availableParentCategories.value = filteredCategories
        }
    }
}
