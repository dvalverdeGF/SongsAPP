package com.grafitto.songsapp.ui.screens

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
    drawerState: DrawerState,
) {
    val categories by viewModel.categories.observeAsState(emptyList())
    val scope = rememberCoroutineScope()
    val filteredCategories = categories.filter { it.parentId == parentId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías") },
                navigationIcon = {
                    Row {
                        if (onBack != null) {
                            IconButton(onClick = { onBack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                            }
                        }
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddCategory(parentId) }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar categoría")
            }
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(filteredCategories) { category ->
                var expanded by remember { mutableStateOf(false) }
                Surface(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    tonalElevation = 2.dp,
                    shadowElevation = 1.dp,
                    onClick = { onNavigateToChildren(category) },
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Menu, contentDescription = null, modifier = Modifier.padding(end = 8.dp))
                            Text(category.name ?: "(Sin nombre)", modifier = Modifier.weight(1f))
                        }
                        Box {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                            }
                            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                DropdownMenuItem(
                                    text = { Text("Editar") },
                                    onClick = {
                                        expanded = false
                                        onEditCategory(category)
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
