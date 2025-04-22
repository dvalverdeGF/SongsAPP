package com.grafitto.songsapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongViewScreen(
    songId: Int,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val database = SongsDatabase.getDatabase(context)
    val repository = SongsRepositoryImpl(database.songDao(), database.verseDao())

    var song by remember { mutableStateOf<Song?>(null) }
    var verses by remember { mutableStateOf<List<Verse>>(emptyList()) }

    LaunchedEffect(songId) {
        withContext(Dispatchers.IO) {
            song = repository.getSongById(songId)

            // Convertir correctamente de VerseEntity a Verse
            val versesResult = repository.getVersesBySongId(songId)
            verses =
                versesResult.map { verseEntity ->
                    Verse(
                        chords = verseEntity.chords,
                        lyrics = verseEntity.lyrics,
                    )
                }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(song?.title ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
        ) {
            song?.let { currentSong ->
                Text(
                    text = currentSong.title,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentSong.artist,
                    style = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(24.dp))

                verses.forEach { verse ->
                    Text(
                        text = verse.lyrics,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
