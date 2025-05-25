package com.grafitto.songsapp.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepository
import com.grafitto.songsapp.ui.screens.CategoryEditScreen
import com.grafitto.songsapp.ui.screens.CategoryListScreen
import com.grafitto.songsapp.ui.screens.MainScreen
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModelFactory

@Suppress("ktlint:standard:function-naming", "detekt.LongMethod")
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
                Box(contentAlignment = Alignment.Center) {
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
                Box(contentAlignment = Alignment.Center) {
                    Text("Pantalla de Repertorios", style = MaterialTheme.typography.headlineMedium)
                }
            }
        }
        composable("settings") {
            Surface(modifier = Modifier.fillMaxSize()) {
                Box(contentAlignment = Alignment.Center) {
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
            CategoryEditScreen(
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
            CategoryEditScreen(
                navController = navController,
                viewModel = categoryViewModelFromParent,
                categoryToEdit = category,
                defaultParentId = category?.parentId,
            )
        }
        composable("category_edit?parentId={parentId}") { backStackEntry ->
            requireNotNull(categoryViewModelFromParent) { "CategoryViewModel must be provided for category_edit route" }

            val parentId = backStackEntry.arguments?.getString("parentId")?.toLongOrNull()
            CategoryEditScreen(
                navController = navController,
                viewModel = categoryViewModelFromParent,
                categoryToEdit = null,
                defaultParentId = parentId,
            )
        }
    }
}
