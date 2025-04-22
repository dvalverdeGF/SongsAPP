// app/src/main/java/com/grafitto/songsapp/ui/screens/CategoryEditScreen.kt
package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grafitto.songsapp.data.model.Category
import com.grafitto.songsapp.ui.viewmodels.CategoryEditViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen(
    categoryId: Int,
    onSaveCategory: (Category) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: CategoryEditViewModel,
) {
    val category by viewModel.category.collectAsState(initial = null)
    val parentCategories by viewModel.availableParentCategories.collectAsState(initial = emptyList())

    LaunchedEffect(categoryId) {
        viewModel.loadCategory(categoryId)
        viewModel.loadAvailableParentCategories(categoryId)
    }

    var name by remember(category) { mutableStateOf(category?.name ?: "") }
    var description by remember(category) { mutableStateOf(category?.description ?: "") }
    var selectedParentId by remember(category) { mutableStateOf(category?.parentId) }
    var expandedMenu by remember { mutableStateOf(false) }

    val isEditMode = categoryId > 0
    val screenTitle = if (isEditMode) "Editar categoría" else "Nueva categoría"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (name.isNotBlank()) {
                                val updatedCategory =
                                    Category(
                                        id = categoryId,
                                        name = name,
                                        description = description,
                                        parentId = selectedParentId,
                                        children = category?.children ?: emptyList(),
                                    )
                                onSaveCategory(updatedCategory)
                            }
                        },
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Guardar")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Campo para el nombre
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre de la categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            // Campo para la descripción
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            // Selector de categoría padre
            ExposedDropdownMenuBox(
                expanded = expandedMenu,
                onExpandedChange = { expandedMenu = it },
            ) {
                TextField(
                    value = parentCategories.find { it.id == selectedParentId }?.name ?: "Sin categoría padre",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría padre") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMenu) },
                    modifier =
                        Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                )

                ExposedDropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false },
                ) {
                    // Opción para "Sin categoría padre"
                    DropdownMenuItem(
                        text = { Text("Sin categoría padre") },
                        onClick = {
                            selectedParentId = null
                            expandedMenu = false
                        },
                        leadingIcon =
                            if (selectedParentId == null) {
                                { Icon(Icons.Default.Check, contentDescription = null) }
                            } else {
                                null
                            },
                    )

                    // Opciones para otras categorías
                    parentCategories.forEach { parentCategory ->
                        // No mostrar la categoría actual como posible padre para evitar ciclos
                        if (parentCategory.id != categoryId) {
                            DropdownMenuItem(
                                text = { Text(parentCategory.name) },
                                onClick = {
                                    selectedParentId = parentCategory.id
                                    expandedMenu = false
                                },
                                leadingIcon =
                                    if (selectedParentId == parentCategory.id) {
                                        { Icon(Icons.Default.Check, contentDescription = null) }
                                    } else {
                                        null
                                    },
                            )
                        }
                    }
                }
            }
        }
    }
}
