// navigation/AppNavigation.kt
package com.grafitto.songsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation(repository: SongsRepository) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            MainScreen(navController = navController, repository = repository)
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
            val categoryId = backStackEntry.arguments?.getString("categoryId")?.toIntOrNull()
            val categories by categoryViewModel.categories.observeAsState(emptyList())
            val category = categories.find { it.id.toInt() == categoryId }
            com.grafitto.songsapp.ui.screens.CategoryEditScreen(
                navController = navController,
                viewModel = categoryViewModel,
                categoryToEdit = category,
            )
        }
    }
}
