package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.flow.collectLatest

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

    LaunchedEffect(categoryToEdit) {
        name = categoryToEdit?.name ?: ""
        parentId = if (categoryToEdit != null) categoryToEdit.parentId else defaultParentId
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
        containerColor = MaterialTheme.colorScheme.background,
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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    )
                    if (showEmptyNameErrorOnSave) {
                        Text(
                            text = "El nombre no puede estar vacío",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    var expanded by remember { mutableStateOf(false) }
                    val parentCategory = allCategories.find { it.id == parentId }
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors =
                            ButtonDefaults.outlinedButtonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.primary,
                            ),
                    ) {
                        Text(parentCategory?.name ?: "Sin categoría padre", color = MaterialTheme.colorScheme.primary)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Sin categoría padre", color = MaterialTheme.colorScheme.onSurface) },
                            onClick = {
                                parentId = null
                                expanded = false
                            },
                        )
                        allCategories.filter { it.id != categoryToEdit?.id }.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name ?: "(Sin nombre)", color = MaterialTheme.colorScheme.onSurface) },
                                onClick = {
                                    parentId = cat.id
                                    expanded = false
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
