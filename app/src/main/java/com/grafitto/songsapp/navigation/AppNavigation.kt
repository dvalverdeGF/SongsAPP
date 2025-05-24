// navigation/AppNavigation.kt
package com.grafitto.songsapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepository
import com.grafitto.songsapp.ui.screens.CategoryListScreen
import com.grafitto.songsapp.ui.screens.MainScreen
import com.grafitto.songsapp.ui.theme.SongsAPPTheme
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation(repository: SongsRepository) {
    SongsAPPTheme(dynamicColor = false) {
        val navController = rememberNavController()
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp
        var showRail by remember { mutableStateOf(screenWidthDp >= 600) }
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        Surface(color = MaterialTheme.colorScheme.background) {
            Row {
                if (showRail) {
                    NavigationRail(
                        modifier = Modifier.shadow(4.dp),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        header = {
                            if (screenWidthDp < 600) {
                                IconButton(onClick = { showRail = false }) {
                                    Icon(Icons.Default.Menu, contentDescription = "Cerrar menú", tint = MaterialTheme.colorScheme.onPrimary)
                                }
                            } else {
                                Text("Menú", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimary)
                            }
                        },
                    ) {
                        NavigationRailItem(
                            selected = navController.currentDestination?.route == "categories",
                            onClick = {
                                showRail = screenWidthDp >= 600
                                navController.navigate("categories") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.Default.List,
                                    contentDescription = "Categorías",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            },
                            label = { Text("Categorías", color = MaterialTheme.colorScheme.onPrimary) },
                            alwaysShowLabel = true,
                        )
                        NavigationRailItem(
                            selected = navController.currentDestination?.route == "opcion1",
                            onClick = {
                                showRail = screenWidthDp >= 600
                                navController.navigate("opcion1") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = "Opción 1",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            },
                            label = { Text("Opción 1", color = MaterialTheme.colorScheme.onPrimary) },
                            alwaysShowLabel = true,
                        )
                        NavigationRailItem(
                            selected = navController.currentDestination?.route == "opcion2",
                            onClick = {
                                showRail = screenWidthDp >= 600
                                navController.navigate("opcion2") {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                    launchSingleTop = true
                                }
                            },
                            icon = {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Opción 2",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                )
                            },
                            label = { Text("Opción 2", color = MaterialTheme.colorScheme.onPrimary) },
                            alwaysShowLabel = true,
                        )
                    }
                }
                Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                    androidx.compose.material3.Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Songs App", color = MaterialTheme.colorScheme.onPrimary) },
                                navigationIcon = {
                                    if (screenWidthDp < 600 && !showRail) {
                                        IconButton(onClick = { showRail = true }) {
                                            Icon(
                                                Icons.Default.Menu,
                                                contentDescription = "Abrir menú",
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
                                scrollBehavior = scrollBehavior,
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.background,
                        contentWindowInsets =
                            androidx.compose.foundation.layout
                                .WindowInsets(0, 0, 0, 0),
                    ) { innerPadding ->
                        Surface(
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = "main",
                            ) {
                                composable("main") {
                                    MainScreen(
                                        navController = navController,
                                        repository = repository,
                                        drawerState = null,
                                    )
                                }
                                composable("categories") {
                                    val context = androidx.compose.ui.platform.LocalContext.current
                                    val db = SongsDatabase.getDatabase(context)
                                    val categoryDao = db.categoryDao()
                                    val categoryViewModel: CategoryViewModel =
                                        viewModel(
                                            factory = CategoryViewModelFactory(categoryDao),
                                        )
                                    CategoryListScreen(
                                        viewModel = categoryViewModel,
                                        onAddCategory = { pid -> navController.navigate("category_edit?parentId=$pid") },
                                        onEditCategory = { navController.navigate("category_edit/${it.id}") },
                                        onNavigateToChildren = { navController.navigate("categories/${it.id}") },
                                        onBack = null,
                                        parentId = null,
                                        drawerState = null,
                                    )
                                }
                                composable("opcion1") {
                                    Surface(modifier = Modifier.fillMaxSize()) {
                                        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                                            Text("Opción 1", style = MaterialTheme.typography.headlineMedium)
                                        }
                                    }
                                }
                                composable("opcion2") {
                                    Surface(modifier = Modifier.fillMaxSize()) {
                                        Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                                            Text("Opción 2", style = MaterialTheme.typography.headlineMedium)
                                        }
                                    }
                                }
                                composable("categories/{parentId}") { backStackEntry ->
                                    val context = androidx.compose.ui.platform.LocalContext.current
                                    val db = SongsDatabase.getDatabase(context)
                                    val categoryDao = db.categoryDao()
                                    val categoryViewModel: CategoryViewModel =
                                        viewModel(
                                            factory = CategoryViewModelFactory(categoryDao),
                                        )
                                    val parentId = backStackEntry.arguments?.getString("parentId")?.toLongOrNull()
                                    CategoryListScreen(
                                        viewModel = categoryViewModel,
                                        onAddCategory = { pid -> navController.navigate("category_edit?parentId=$pid") },
                                        onEditCategory = { navController.navigate("category_edit/${it.id}") },
                                        onNavigateToChildren = { navController.navigate("categories/${it.id}") },
                                        onBack =
                                            if (parentId != null) {
                                                { navController.popBackStack() }
                                            } else {
                                                null
                                            },
                                        parentId = parentId,
                                        drawerState = null,
                                    )
                                }
                                composable("category_edit") {
                                    val context = androidx.compose.ui.platform.LocalContext.current
                                    val db = SongsDatabase.getDatabase(context)
                                    val categoryDao = db.categoryDao()
                                    val categoryViewModel: CategoryViewModel =
                                        viewModel(
                                            factory = CategoryViewModelFactory(categoryDao),
                                        )
                                    com.grafitto.songsapp.ui.screens.CategoryEditScreen(
                                        navController = navController,
                                        viewModel = categoryViewModel,
                                        categoryToEdit = null,
                                        drawerState = null,
                                    )
                                }
                                composable("category_edit/{categoryId}") { backStackEntry ->
                                    val context = androidx.compose.ui.platform.LocalContext.current
                                    val db = SongsDatabase.getDatabase(context)
                                    val categoryDao = db.categoryDao()
                                    val categoryViewModel: CategoryViewModel =
                                        viewModel(
                                            factory = CategoryViewModelFactory(categoryDao),
                                        )
                                    val categoryId = backStackEntry.arguments?.getString("categoryId")?.toLongOrNull()
                                    val categoriesState = categoryViewModel.categories.observeAsState(emptyList())
                                    val categories = categoriesState.value
                                    val category = categories.find { it.id == categoryId }
                                    com.grafitto.songsapp.ui.screens.CategoryEditScreen(
                                        navController = navController,
                                        viewModel = categoryViewModel,
                                        categoryToEdit = category,
                                        drawerState = null,
                                    )
                                }
                                composable("category_edit?parentId={parentId}") { backStackEntry ->
                                    val context = androidx.compose.ui.platform.LocalContext.current
                                    val db = SongsDatabase.getDatabase(context)
                                    val categoryDao = db.categoryDao()
                                    val categoryViewModel: CategoryViewModel =
                                        viewModel(
                                            factory = CategoryViewModelFactory(categoryDao),
                                        )
                                    val parentId = backStackEntry.arguments?.getString("parentId")?.toLongOrNull()
                                    com.grafitto.songsapp.ui.screens.CategoryEditScreen(
                                        navController = navController,
                                        viewModel = categoryViewModel,
                                        categoryToEdit = null,
                                        drawerState = null,
                                        defaultParentId = parentId,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
