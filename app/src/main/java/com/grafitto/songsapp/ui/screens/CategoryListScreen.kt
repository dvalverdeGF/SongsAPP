package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel

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

    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    val isTablet = screenWidthDp >= 600

    Scaffold(
        topBar = {
            if (isTablet) {
                // Solo mostrar TopAppBar en tablet/escritorio
                TopAppBar(
                    title = { Text(parentCategory?.name ?: "Categorías", style = MaterialTheme.typography.titleLarge) },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
            color = MaterialTheme.colorScheme.background,
        ) {
            if (filteredCategories.isEmpty()) {
                // Imagen ilustrativa y mensaje amigable
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_gallery),
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "No hay categorías aún",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Toca el botón + para crear tu primera categoría",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    )
                }
            } else {
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = 0.dp, vertical = 0.dp),
                    verticalArrangement = Arrangement.Top,
                ) {
                    items(filteredCategories) { category ->
                        var expanded by remember { mutableStateOf(false) }
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .clickable { onNavigateToChildren(category) }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                category.name ?: "(Sin nombre)",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f),
                            )
                            Box {
                                IconButton(onClick = { expanded = true }) {
                                    Icon(
                                        Icons.Default.MoreVert,
                                        contentDescription = "Opciones",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                    )
                                }
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(
                                        text = { Text("Editar", color = MaterialTheme.colorScheme.onSurface) },
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
