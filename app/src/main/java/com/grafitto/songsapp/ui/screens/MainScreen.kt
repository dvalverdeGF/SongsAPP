package com.grafitto.songsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.grafitto.songsapp.data.model.Song
import com.grafitto.songsapp.data.repository.SongsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    repository: SongsRepository,
    drawerState: DrawerState?, // Puede ser null, ya no se usa
) {
    Scaffold(
        topBar = {}, // Sin header fijo en móvil
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("edit_song")
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crear nueva canción",
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        MainContent(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            repository = repository,
        )
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    repository: SongsRepository, // Asegúrate que SongsRepository esté bien importado
) {
    val songsFlow: Flow<List<Song>> = repository.getAllSongs()
    val songs by songsFlow
        .catch { exception ->
            Log.e("MainScreen", "Error collecting songs", exception)
            emit(emptyList<Song>())
        }.collectAsState(initial = emptyList<Song>())

    if (songs.isEmpty()) {
        // Imagen ilustrativa y mensaje amigable estilo Google Home
        Column(
            modifier = modifier.fillMaxSize(),
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
                text = "No hay canciones aún",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toca el botón + para crear tu primera canción",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            )
        }
    } else {
        // Aquí iría el listado de canciones, usando colores y superficies por defecto
        // ...
    }
}
