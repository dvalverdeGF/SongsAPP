// navigation/AppNavigation.kt
package com.grafitto.songsapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
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

data class NavItemData(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val onClick: () -> Unit,
)

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

        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        val categoryViewModelFromAppNavigation: CategoryViewModel? =
            if (currentRoute?.startsWith("category_edit") == true) {
                viewModel(
                    key = "category_edit_vm",
                    factory =
                        CategoryViewModelFactory(
                            LocalContext.current.let {
                                SongsDatabase.getDatabase(it).categoryDao()
                            },
                        ),
                )
            } else {
                null
            }

        val baseNavItems =
            listOf(
                Triple("main", Icons.Default.Home, "Inicio"),
                Triple("songs", Icons.Default.MusicNote, "Canciones"),
                Triple("categories", Icons.AutoMirrored.Filled.List, "Categorías"),
                Triple("repertoires", Icons.Default.LibraryMusic, "Repertorios"),
                Triple("settings", Icons.Default.Settings, "Configuración"),
            )

        val dynamicNavItems = getDynamicNavItems(navController, currentRoute, baseNavItems, categoryViewModelFromAppNavigation)

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
                        dynamicNavItems.forEach { item ->
                            NavigationRailItem(
                                selected = currentRoute == item.route,
                                onClick = item.onClick,
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) },
                                alwaysShowLabel = true,
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                        Scaffold(
                            contentWindowInsets =
                                androidx.compose.foundation.layout
                                    .WindowInsets(0, 0, 0, 0),
                        ) { innerPadding ->
                            AppNavHost(navController, repository, Modifier.padding(innerPadding), categoryViewModelFromAppNavigation)
                        }
                    }
                }
            } else {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentWindowInsets =
                        androidx.compose.foundation.layout
                            .WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = NavigationBarDefaults.Elevation,
                        ) {
                            dynamicNavItems.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = item.onClick,
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) },
                                    alwaysShowLabel = true,
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    AppNavHost(navController, repository, Modifier.padding(innerPadding), categoryViewModelFromAppNavigation)
                }
            }
        }
    }
}

@Composable
fun getDynamicNavItems(
    navController: NavHostController,
    currentRoute: String?,
    baseNavItems: List<Triple<String, androidx.compose.ui.graphics.vector.ImageVector, String>>,
    categoryViewModel: CategoryViewModel?,
): List<NavItemData> {
    val defaultNavOnClick: (String) -> () -> Unit = { route ->
        {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    val isCategoriesSectionActive =
        currentRoute != null &&
            (
                currentRoute == "categories" ||
                    currentRoute.startsWith("categories/") ||
                    currentRoute.startsWith("category_edit")
            )

    val categoriesNavItem =
        NavItemData(
            route = if (isCategoriesSectionActive && currentRoute != null) currentRoute else "categories",
            icon = Icons.AutoMirrored.Filled.List,
            label = "Categorías",
            onClick = defaultNavOnClick("categories"),
        )

    return when {
        currentRoute == "categories" ||
            (
                currentRoute?.startsWith("categories/") == true &&
                    currentRoute != "category_edit" &&
                    !currentRoute.startsWith("category_edit/")
            ) -> {
            val items = mutableListOf<NavItemData>()
            items.add(NavItemData("main", Icons.Default.Home, "Inicio", defaultNavOnClick("main")))
            items.add(categoriesNavItem)
            items.add(
                NavItemData("category_create", Icons.Default.Add, "Crear") {
                    val parentIdArg =
                        if (currentRoute.startsWith("categories/")) {
                            navController.currentBackStackEntry?.arguments?.getString("parentId")
                        } else {
                            null
                        }
                    if (parentIdArg != null) {
                        navController.navigate("category_edit?parentId=$parentIdArg")
                    } else {
                        navController.navigate("category_edit")
                    }
                },
            )
            if (currentRoute.startsWith("categories/") && navController.previousBackStackEntry != null) {
                items.add(NavItemData("back", Icons.AutoMirrored.Filled.ArrowBack, "Atrás") { navController.popBackStack() })
            }
            items
        }
        currentRoute?.startsWith("category_edit") == true -> {
            listOfNotNull(
                NavItemData("main", Icons.Default.Home, "Inicio", defaultNavOnClick("main")),
                categoriesNavItem,
                categoryViewModel?.let { vm -> NavItemData("category_save", Icons.Filled.CloudUpload, "Guardar") { vm.requestSave() } },
                NavItemData("back", Icons.AutoMirrored.Filled.ArrowBack, "Atrás") { navController.popBackStack() },
            )
        }
        else -> {
            baseNavItems.map { (baseRoute, icon, label) ->
                if (baseRoute == "categories") {
                    categoriesNavItem
                } else {
                    NavItemData(baseRoute, icon, label, defaultNavOnClick(baseRoute))
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavHost(
    navController: NavHostController,
    repository: SongsRepository,
    modifier: Modifier,
    categoryViewModelFromParent: CategoryViewModel?,
) {
    NavHost(
        navController = navController,
        startDestination = "main",
        modifier = modifier,
    ) {
        composable("main") {
            MainScreen(
                navController = navController,
                repository = repository,
            )
        }
        composable("songs") {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Pantalla de Canciones", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
        composable("categories") {
            val context = LocalContext.current
            val db = SongsDatabase.getDatabase(context)
            val categoryDao = db.categoryDao()
            val categoryViewModel: CategoryViewModel = viewModel(factory = CategoryViewModelFactory(categoryDao))
            CategoryListScreen(
                viewModel = categoryViewModel,
                onEditCategory = { cat -> navController.navigate("category_edit/${cat.id}") },
                onNavigateToChildren = { cat -> navController.navigate("categories/${cat.id}") },
                parentId = null,
            )
        }
        composable("repertoires") {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Pantalla de Repertorios", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
        composable("settings") {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text("Pantalla de Configuración", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
        composable("categories/{parentId}") { backStackEntry ->
            val context = LocalContext.current
            val db = SongsDatabase.getDatabase(context)
            val categoryDao = db.categoryDao()
            val categoryViewModel: CategoryViewModel = viewModel(factory = CategoryViewModelFactory(categoryDao))
            val parentId = backStackEntry.arguments?.getString("parentId")?.toLongOrNull()
            CategoryListScreen(
                viewModel = categoryViewModel,
                onEditCategory = { cat -> navController.navigate("category_edit/${cat.id}") },
                onNavigateToChildren = { cat -> navController.navigate("categories/${cat.id}") },
                parentId = parentId,
            )
        }
        composable("category_edit") {
            requireNotNull(categoryViewModelFromParent) { "CategoryViewModel must be provided for category_edit route" }
            com.grafitto.songsapp.ui.screens.CategoryEditScreen(
                navController = navController,
                viewModel = categoryViewModelFromParent,
                categoryToEdit = null,
                defaultParentId = null,
            )
        }
        composable("category_edit/{categoryId}") { backStackEntry ->
            requireNotNull(categoryViewModelFromParent) { "CategoryViewModel must be provided for category_edit route" }

            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toLongOrNull()
            val categoriesState = categoryViewModelFromParent.categories.observeAsState(emptyList())
            val categories = categoriesState.value
            val category = categories.find { it.id == categoryId }
            com.grafitto.songsapp.ui.screens.CategoryEditScreen(
                navController = navController,
                viewModel = categoryViewModelFromParent,
                categoryToEdit = category,
            )
        }
        composable("category_edit?parentId={parentId}") { backStackEntry ->
            requireNotNull(categoryViewModelFromParent) { "CategoryViewModel must be provided for category_edit route" }

            val parentId = backStackEntry.arguments?.getString("parentId")?.toLongOrNull()
            com.grafitto.songsapp.ui.screens.CategoryEditScreen(
                navController = navController,
                viewModel = categoryViewModelFromParent,
                categoryToEdit = null,
                defaultParentId = parentId,
            )
        }
    }
}
