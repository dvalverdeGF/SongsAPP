package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryListScreen(
    viewModel: CategoryViewModel,
    onAddCategory: (Long?) -> Unit,
    onEditCategory: (Category) -> Unit,
    onNavigateToChildren: (Category) -> Unit,
    onBack: (() -> Unit)? = null,
    parentId: Long? = null,
    drawerState: DrawerState?,
) {
    val categories by viewModel.categories.observeAsState(emptyList())
    val scope = rememberCoroutineScope()
    val filteredCategories = categories.filter { it.parentId == parentId }
    val parentCategory = categories.find { it.id == parentId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(parentCategory?.name ?: "Categorías", color = MaterialTheme.colorScheme.onPrimary) },
                navigationIcon = {
                    // Solo mostrar el icono de menú si drawerState no es null (modo drawer antiguo)
                    if (drawerState != null) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
                    }
                },
                actions = {
                    if (onBack != null) {
                        IconButton(onClick = { onBack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Atrás",
                                tint = MaterialTheme.colorScheme.onPrimary,
                            )
                        }
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
                onClick = { onAddCategory(parentId) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Agregar categoría",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
    ) { padding ->
        if (filteredCategories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Text(
                    text = "No hay categorías",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                items(filteredCategories) { category ->
                    var expanded by remember { mutableStateOf(false) }
                    Column {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clickable { onNavigateToChildren(category) },
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                category.name ?: "(Sin nombre)",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Box {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = "Opciones",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                    )
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Editar", color = MaterialTheme.colorScheme.onPrimary) },
                                        onClick = {
                                            expanded = false
                                            onEditCategory(category)
                                        },
                                    )
                                }
                            }
                        }
                        Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), thickness = 1.dp)
                    }
                }
            }
        }
    }
}
