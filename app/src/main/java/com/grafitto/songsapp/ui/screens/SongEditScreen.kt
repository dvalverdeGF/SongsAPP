// ui/screens/SongEditScreen.kt
package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.model.Song
import com.grafitto.songsapp.data.model.Verse
import com.grafitto.songsapp.data.repository.SongsRepositoryImpl
import com.grafitto.songsapp.ui.components.VerseEditor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongEditScreen(
    songId: Int = 0, // 0 para canción nueva, otro valor para editar
    onSaveSong: (Song) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val database = SongsDatabase.getDatabase(context)
    val repository = SongsRepositoryImpl(database.songDao(), database.verseDao(), database.categoryDao())

    var existingSong by remember { mutableStateOf<Song?>(null) }

    // Cargar la canción existente si se está editando
    LaunchedEffect(songId) {
        if (songId > 0) {
            withContext(Dispatchers.IO) {
                existingSong = repository.getSongById(songId)
            }
        }
    }

    // Inicializar estados con valores de la canción existente o valores predeterminados
    var title by remember(existingSong) { mutableStateOf(existingSong?.title ?: "") }
    var artist by remember(existingSong) { mutableStateOf(existingSong?.artist ?: "") }
    var verses by remember(existingSong) { mutableStateOf(existingSong?.verses ?: listOf(Verse())) }

    // Cambiar el título según si es edición o creación
    val screenTitle = if (songId > 0) "Editar canción" else "Crear canción"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (title.isNotBlank() && artist.isNotBlank()) {
                                val song =
                                    Song(
                                        id = 0, // Se asignará en el repositorio
                                        title = title,
                                        artist = artist,
                                        verses = verses,
                                    )
                                onSaveSong(song)
                            } else {
                                // Mostrar un mensaje de error o manejar el caso
                            }
                        },
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Guardar")
                    }
                },
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // Campos de título y artista
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    // No usar restricciones en el tipo de teclado
                    keyboardOptions = KeyboardOptions.Default,
                    singleLine = true,
                )
            }

            item {
                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Artista") },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                    keyboardOptions = KeyboardOptions.Default,
                    singleLine = true,
                )
            }

            // Lista de versos
            items(verses.size) { index ->
                VerseEditor(
                    verse = verses[index],
                    onVerseChange = { updatedVerse ->
                        verses =
                            verses.toMutableList().apply {
                                this[index] = updatedVerse
                            }
                    },
                    onChordLineClick = {
                        // Aquí implementaremos la funcionalidad para editar acordes
                    },
                )
            }

            // Botón para añadir un nuevo verso
            item {
                Button(
                    onClick = {
                        verses = verses + Verse()
                    },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(text = "Añadir verso", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
