package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEditScreen(
    navController: NavController,
    viewModel: CategoryViewModel,
    categoryToEdit: Category? = null,
    drawerState: DrawerState?,
    defaultParentId: Long? = null,
) {
    var name by remember { mutableStateOf(categoryToEdit?.name ?: "") }
    val isEdit = categoryToEdit != null
    val allCategories by viewModel.categories.observeAsState(emptyList())
    var parentId by remember { mutableStateOf(if (isEdit) categoryToEdit?.parentId else defaultParentId) }
    val scope = rememberCoroutineScope()

    // Sincronizar el estado cuando categoryToEdit cambie
    LaunchedEffect(categoryToEdit) {
        name = categoryToEdit?.name ?: ""
        parentId = if (isEdit) categoryToEdit?.parentId else defaultParentId
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEdit) "Editar Categoría" else "Nueva Categoría",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState?.open() } }) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val category =
                        if (isEdit) {
                            categoryToEdit!!.copy(name = name, parentId = parentId)
                        } else {
                            Category(name = name, parentId = parentId)
                        }
                    if (isEdit) {
                        viewModel.updateCategory(category)
                    } else {
                        viewModel.insertCategory(category)
                    }
                    navController.popBackStack()
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(Icons.Default.Check, contentDescription = "Guardar", tint = MaterialTheme.colorScheme.onPrimary)
            }
        },
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
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = true,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    // Dropdown para seleccionar categoría padre
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
                            text = { Text("Sin categoría padre", color = MaterialTheme.colorScheme.onPrimary) },
                            onClick = {
                                parentId = null
                                expanded = false
                            },
                        )
                        allCategories.filter { it.id != categoryToEdit?.id }.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat.name ?: "(Sin nombre)", color = MaterialTheme.colorScheme.onPrimary) },
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
