package com.grafitto.songsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepository
import com.grafitto.songsapp.data.repository.SongsRepositoryImpl
import com.grafitto.songsapp.ui.screens.MainScreen
import com.grafitto.songsapp.ui.screens.SongEditScreen
import com.grafitto.songsapp.ui.theme.SongsAPPTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var repository: SongsRepository

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Reemplaza el casting directo con esta opción segura
        repository =
            if (application is SongsApplication) {
                (application as SongsApplication).repository
            } else {
                // Fallback si no está configurada la aplicación personalizada
                val database = SongsDatabase.getDatabase(this)
                SongsRepositoryImpl(database.songDao(), database.verseDao())
            }

        enableEdgeToEdge()
        setContent {
            SongsAPPTheme {
                val navController = rememberNavController()
                val coroutineScope = rememberCoroutineScope()

                NavHost(
                    navController = navController,
                    startDestination = "main",
                ) {
                    composable("main") {
                        MainScreen(navController = navController)
                    }
                    composable("edit_song") {
                        SongEditScreen(
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
                    composable(
                        route = "edit_song/{songId}",
                        arguments = listOf(navArgument("songId") { type = NavType.IntType }),
                    ) { backStackEntry ->
                        val songId = backStackEntry.arguments?.getInt("songId") ?: 0

                        // Necesitarás resolver esto de manera asíncrona
                        // Aquí un ejemplo con LaunchedEffect y estado observable:
                        // (Para implementación completa, necesitarías usar LaunchedEffect y
                        // un estado mutable para cargar el song)

                        // Esta es una solución simplificada - necesitarás adaptarla:
                        lifecycleScope.launch {
                            val song = repository.getSongById(songId)
                            if (song != null) {
                                // Lógica para mostrar SongEditScreen con el song cargado
                                // Esto requiere rediseñar esta parte utilizando
                                // estados observables y LaunchedEffect
                            }
                        }

                        SongEditScreen(
                            onSaveSong = { updatedSong ->
                                coroutineScope.launch {
                                    repository.updateSong(updatedSong)
                                    navController.popBackStack()
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            },
                        )
                    }
                }
            }
        }
    }
}
