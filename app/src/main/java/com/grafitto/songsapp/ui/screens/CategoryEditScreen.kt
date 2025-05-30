package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.flow.collectLatest

// Helper function to check if 'category' is a descendant of 'ancestor'
// This prevents selecting a category's own descendant as its parent.
private fun isDescendant(
    category: Category,
    ancestor: Category?,
    allCategories: List<Category>,
): Boolean {
    if (ancestor == null) return false
    var currentParentId = category.parentId
    while (currentParentId != null) {
        if (currentParentId == ancestor.id) {
            return true
        }
        val parentCategory = allCategories.find { it.id == currentParentId } ?: return false // Should not happen with consistent data
        currentParentId = parentCategory.parentId
    }
    return false
}

// Helper function to get the hierarchical path string for a category
private fun getFormattedCategoryPath(
    category: Category?,
    allCategories: List<Category>,
    defaultText: String = "Sin categoría padre",
): String {
    if (category == null) return defaultText

    val path = mutableListOf<String>()
    var current: Category? = category
    while (current != null) {
        path.add(0, current.name ?: "(Sin nombre)")
        if (current.parentId == null) break
        val parent = allCategories.find { it.id == current.parentId }
        // Basic cycle detection or very deep path
        if (parent == current || path.size > 10) { // Safety break
            path.add(0, "...")
            break
        }
        current = parent
    }
    return path.joinToString(" > ")
}

// Helper function to get the depth of a category
private fun getCategoryDepth(
    category: Category?,
    allCategories: List<Category>,
): Int {
    if (category?.parentId == null) return 0 // Categoría raíz o nula tiene profundidad 0
    var depth = 0
    var currentParentId = category.parentId
    val visitedIds = mutableSetOf<Long>() // Para evitar ciclos infinitos

    while (currentParentId != null) {
        if (!visitedIds.add(currentParentId)) { // Ciclo detectado
            // Podría ser un error en los datos o una jerarquía inesperadamente compleja
            break
        }
        depth++
        val parent = allCategories.find { it.id == currentParentId } ?: break // Padre no encontrado
        currentParentId = parent.parentId
        if (depth > 20) break // Límite de profundidad para seguridad y rendimiento
    }
    return depth
}

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen(
    navController: NavController,
    viewModel: CategoryViewModel,
    categoryToEdit: Category? = null,
    defaultParentId: Long? = null,
) {
    var name by remember { mutableStateOf(categoryToEdit?.name ?: "") }
    val allCategories by viewModel.categories.observeAsState(emptyList())
    var parentId by remember { mutableStateOf(if (categoryToEdit != null) categoryToEdit.parentId else defaultParentId) }
    var showEmptyNameErrorOnSave by remember { mutableStateOf(false) }

    var expandedDropdown by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") } // Estado para la consulta de búsqueda

    LaunchedEffect(categoryToEdit, defaultParentId) {
        // Asegurarse de que defaultParentId también reinicie el estado
        name = categoryToEdit?.name ?: ""
        parentId = categoryToEdit?.parentId ?: defaultParentId
        showEmptyNameErrorOnSave = false
    }

    LaunchedEffect(Unit) {
        viewModel.saveRequestFlow.collectLatest {
            val success = viewModel.saveCategory(name, categoryToEdit, parentId)
            if (success) {
                navController.popBackStack()
            } else {
                showEmptyNameErrorOnSave = true
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow, // Fondo de la pantalla
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer), // Color para el Card
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (it.isNotBlank()) {
                                showEmptyNameErrorOnSave = false
                            }
                        },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                        isError = showEmptyNameErrorOnSave,
                        colors =
                            TextFieldDefaults.colors(
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                                focusedLabelColor = MaterialTheme.colorScheme.primary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                errorContainerColor = Color.Transparent,
                            ),
                    )
                    if (showEmptyNameErrorOnSave) {
                        Text(
                            text = "El nombre no puede estar vacío",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    // Selector de Categoría Padre con ExposedDropdownMenuBox
                    ExposedDropdownMenuBox(
                        expanded = expandedDropdown,
                        onExpandedChange = {
                            expandedDropdown = !expandedDropdown
                            if (expandedDropdown) {
                                searchQuery = "" // Reiniciar la búsqueda al abrir el menú
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        val parentCategory = allCategories.find { it.id == parentId }
                        OutlinedTextField(
                            modifier = Modifier.menuAnchor().fillMaxWidth(), // Importante para ExposedDropdownMenuBox
                            readOnly = true,
                            value = getFormattedCategoryPath(parentCategory, allCategories), // Muestra la ruta completa aquí
                            onValueChange = {}, // No se necesita para un campo de solo lectura
                            label = { Text("Categoría Padre") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                            colors =
                                ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                    focusedTextColor = MaterialTheme.colorScheme.onSurface, // Parámetro corregido
                                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface, // Parámetro corregido
                                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                                    unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    focusedTrailingIconColor = MaterialTheme.colorScheme.primary,
                                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                        )
                        ExposedDropdownMenu(
                            expanded = expandedDropdown,
                            onDismissRequest = { expandedDropdown = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        ) {
                            // Campo de búsqueda dentro del menú desplegable
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                placeholder = { Text("Buscar categoría...") },
                                singleLine = true,
                                colors =
                                    TextFieldDefaults.colors(
                                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                                        cursorColor = MaterialTheme.colorScheme.primary,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        errorContainerColor = Color.Transparent,
                                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    ),
                            )

                            // Opción para "Sin categoría padre"
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        getFormattedCategoryPath(null, allCategories, "Ninguna (Categoría Raíz)"),
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )
                                },
                                onClick = {
                                    parentId = null
                                    expandedDropdown = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )

                            val selectableCategories =
                                allCategories.filter { cat ->
                                    cat.id != categoryToEdit?.id &&
                                        !isDescendant(cat, categoryToEdit, allCategories)
                                }

                            val categoriesToDisplay =
                                if (searchQuery.isBlank()) {
                                    selectableCategories
                                } else {
                                    selectableCategories.filter { cat ->
                                        (cat.name ?: "").contains(searchQuery, ignoreCase = true) ||
                                            // Buscar por nombre
                                            getFormattedCategoryPath(cat, allCategories).contains(searchQuery, ignoreCase = true) // Buscar por ruta
                                    }
                                }

                            if (categoriesToDisplay.isEmpty()) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            if (searchQuery.isNotBlank()) {
                                                "No se encontraron categorías"
                                            } else {
                                                "No hay categorías seleccionables"
                                            },
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    },
                                    onClick = {},
                                    enabled = false,
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }

                            categoriesToDisplay.forEach { cat ->
                                DropdownMenuItem(
                                    text = {
                                        val depth = getCategoryDepth(cat, allCategories)
                                        val prefix = "  ".repeat(depth) // Indentación con espacios
                                        val displayName = cat.name ?: "(Sin nombre)"
                                        Text(prefix + displayName, color = MaterialTheme.colorScheme.onSurface)
                                    },
                                    onClick = {
                                        parentId = cat.id
                                        expandedDropdown = false
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
