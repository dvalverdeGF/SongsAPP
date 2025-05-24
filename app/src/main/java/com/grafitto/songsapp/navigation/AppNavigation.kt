// navigation/AppNavigation.kt
package com.grafitto.songsapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grafitto.songsapp.data.model.Song
import com.grafitto.songsapp.ui.screens.MainScreen
import com.grafitto.songsapp.ui.screens.SongEditScreen
import com.grafitto.songsapp.ui.screens.SongViewScreen
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@Composable
fun AppNavigation(repository: SongsRepository) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "main",
    ) {
        composable("main") {
            MainScreen(navController = navController)
        }

        // Ruta para crear una nueva canción
        composable("edit_song") {
            SongEditScreen(
                songId = 0, // 0 indica una canción nueva
                onSaveSong = { song ->
                    coroutineScope.launch {
                        repository.addSong(song)
                        navController.popBackStack()
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        // Ruta para editar una canción existente
        composable(
            route = "edit_song/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getInt("songId") ?: 0

            SongEditScreen(
                songId = songId,
                onSaveSong = { updatedSong ->
                    coroutineScope.launch {
                        // Usamos el ID original al actualizar
                        val songWithOriginalId =
                            Song(
                                id = songId,
                                title = updatedSong.title,
                                artist = updatedSong.artist,
                                verses = updatedSong.verses,
                            )
                        repository.updateSong(songWithOriginalId)
                        navController.popBackStack()
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        // Ruta para visualizar una canción
        composable(
            route = "view_song/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.IntType }),
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getInt("songId") ?: 0

            SongViewScreen(
                songId = songId,
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}
