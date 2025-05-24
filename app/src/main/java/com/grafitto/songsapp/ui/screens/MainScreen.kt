package com.grafitto.songsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.model.Song
import com.grafitto.songsapp.ui.components.EmptySongsMessage
import com.grafitto.songsapp.ui.components.SongsList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController = rememberNavController()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menú", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = { Text("Opción 1") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                )
                NavigationDrawerItem(
                    label = { Text("Opción 2") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Songs App") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                            )
                        }
                    },
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate("edit_song")
                    },
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Crear nueva canción",
                    )
                }
            },
            modifier = Modifier.fillMaxSize(),
        ) { innerPadding ->
            MainContent(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    // Obtener el contexto
    val context = LocalContext.current

    // Obtener la base de datos
    val database = SongsDatabase.getDatabase(context)

    // Obtener los DAOs
    val songDao = database.songDao()
    val verseDao = database.verseDao()

    // Crear el repositorio
    val repository: SongsRepository =
        SongsRepositoryImpl(
            database.songDao(),
            database.verseDao(),
            database.categoryDao(),
        )

    // Usar collectAsState con manejo de errores
    val songs by repository
        .getAllSongs()
        .catch { exception ->
            Log.e("MainScreen", "Error collecting songs", exception)
            emit(emptyList<Song>())
        }.collectAsState(initial = emptyList<Song>())

    if (songs.isEmpty()) {
        EmptySongsMessage(modifier)
    } else {
        SongsList(
            songs = songs,
            modifier = modifier,
            onSongClick = { /* Ahora solo se usa para expandir/contraer */ },
            onSongEdit = { song ->
                // Navegar a la pantalla de edición
                navController.navigate("edit_song/${song.id}")
            },
            onSongView = { song ->
                // Navegar a la pantalla de visualización en modo texto
                navController.navigate("view_song/${song.id}")
            },
        )
    }
}
