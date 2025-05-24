// navigation/AppNavigation.kt
package com.grafitto.songsapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepository
import com.grafitto.songsapp.ui.screens.CategoryListScreen
import com.grafitto.songsapp.ui.screens.MainScreen
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModelFactory
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation(repository: SongsRepository) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Categorías") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("categories")
                    },
                )
                NavigationDrawerItem(
                    label = { Text("Opción 1") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                )
                NavigationDrawerItem(
                    label = { Text("Opción 2") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                    },
                )
            }
        },
    ) {
        NavHost(
            navController = navController,
            startDestination = "main",
        ) {
            composable("main") {
                MainScreen(navController = navController, repository = repository, drawerState = drawerState)
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
                    navController = navController,
                    viewModel = categoryViewModel,
                    onAddCategory = { navController.navigate("category_edit") },
                    onEditCategory = { navController.navigate("category_edit/${it.id}") },
                    onDeleteCategory = { categoryViewModel.deleteCategory(it) },
                    drawerState = drawerState,
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
                    drawerState = drawerState,
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
                    drawerState = drawerState,
                )
            }
        }
    }
}
