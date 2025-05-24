// navigation/AppNavigation.kt
package com.grafitto.songsapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
        val isTablet = screenWidthDp >= 600
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

        val navItems =
            listOf(
                Triple("main", Icons.Default.Home, "Inicio"),
                Triple("categories", Icons.Default.List, "Categorías"),
                Triple("opcion1", Icons.Default.Settings, "Ajustes"),
                Triple("opcion2", Icons.Default.Info, "Info"),
            )

        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route
        val headerTitle =
            when {
                currentRoute == null || currentRoute == "main" -> "Songs App"
                currentRoute.startsWith("categories") && !currentRoute.contains("edit") -> "Categorías"
                currentRoute == "categories" -> "Categorías"
                currentRoute == "categories/{parentId}" -> "Categorías"
                currentRoute.startsWith("category_edit") -> {
                    val isEdit = currentRoute.contains("category_edit/") && !currentRoute.contains("parentId")
                    if (isEdit) "Editar Categoría" else "Nueva Categoría"
                }
                currentRoute == "opcion1" -> "Ajustes"
                currentRoute == "opcion2" -> "Info"
                else -> "Songs App"
            }

        Surface(color = MaterialTheme.colorScheme.background) {
            if (isTablet) {
                Row {
                    NavigationRail(
                        modifier = Modifier.shadow(4.dp),
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        header = {
                            Text("SongsApp", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.primary)
                        },
                    ) {
                        navItems.forEach { (route, icon, label) ->
                            NavigationRailItem(
                                selected = currentRoute == route,
                                onClick = {
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                },
                                icon = { Icon(icon, contentDescription = label) },
                                label = { Text(label) },
                                alwaysShowLabel = true,
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                        Scaffold(
                            topBar = {
                                TopAppBar(
                                    title = { Text(headerTitle, color = MaterialTheme.colorScheme.onPrimary) },
                                    colors =
                                        TopAppBarDefaults.topAppBarColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                        ),
                                    scrollBehavior = scrollBehavior,
                                )
                            },
                            //  containerColor = MaterialTheme.colorScheme.background,
                            contentWindowInsets =
                                androidx.compose.foundation.layout
                                    .WindowInsets(0, 0, 0, 0),
                            bottomBar = {},
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
                                            onAddCategory = { navController.navigate("category_edit") },
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
                                            onAddCategory = { pId -> navController.navigate("category_edit?parentId=$pId") },
                                            onEditCategory = { navController.navigate("category_edit/${it.id}") },
                                            onNavigateToChildren = { navController.navigate("categories/${it.id}") },
                                            onBack = { navController.popBackStack() },
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
                                            defaultParentId = null,
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
                                            defaultParentId = parentId,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Móvil: Bottom Navigation estilo Google Home
                Scaffold(
                    topBar = {
                        val currentDestinationRoute = currentBackStackEntry?.destination?.route
                        // Solo mostrar el TopAppBar global si no estamos en una pantalla de edición de categoría
                        if (currentDestinationRoute?.startsWith("category_edit") != true) {
                            TopAppBar(
                                title = { }, // Título eliminado para una barra más estrecha
                                colors =
                                    TopAppBarDefaults.topAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                                    ),
                                scrollBehavior = scrollBehavior,
                                navigationIcon = {
                                    if (navController.previousBackStackEntry != null) {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Atrás",
                                                // tint = MaterialTheme.colorScheme.onPrimary // Hereda de actionIconContentColor
                                            )
                                        }
                                    }
                                },
                                actions = {
                                    if (currentDestinationRoute == "categories" || currentDestinationRoute == "categories/{parentId}") {
                                        IconButton(onClick = {
                                            if (currentDestinationRoute == "categories") {
                                                navController.navigate("category_edit") // Add root category
                                            } else { // currentDestinationRoute == "categories/{parentId}"
                                                val parentIdArg = currentBackStackEntry?.arguments?.getString("parentId")
                                                navController.navigate("category_edit?parentId=$parentIdArg")
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Filled.Add,
                                                contentDescription = "Añadir Categoría",
                                                // tint = MaterialTheme.colorScheme.onPrimary // Hereda de actionIconContentColor
                                            )
                                        }
                                    }
                                },
                            )
                        } else {
                            // No se muestra TopAppBar aquí; CategoryEditScreen tiene el suyo.
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.background,
                    contentWindowInsets =
                        androidx.compose.foundation.layout
                            .WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = NavigationBarDefaults.Elevation,
                        ) {
                            navItems.forEach { (route, icon, label) ->
                                NavigationBarItem(
                                    selected = currentRoute == route,
                                    onClick = {
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    },
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label) },
                                    alwaysShowLabel = true,
                                )
                            }
                        }
                    },
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
                                    onAddCategory = { navController.navigate("category_edit") },
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
                                    onAddCategory = { pId -> navController.navigate("category_edit?parentId=$pId") },
                                    onEditCategory = { navController.navigate("category_edit/${it.id}") },
                                    onNavigateToChildren = { navController.navigate("categories/${it.id}") },
                                    onBack = { navController.popBackStack() },
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
                                    defaultParentId = null,
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
