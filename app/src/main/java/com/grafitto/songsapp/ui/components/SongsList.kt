package com.grafitto.songsapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grafitto.songsapp.data.model.Song

@Suppress("ktlint:standard:function-naming")
@Composable
fun SongsList(
    songs: List<Song>,
    modifier: Modifier = Modifier,
    onSongClick: (Song) -> Unit,
    onSongEdit: (Song) -> Unit = {},
    onSongView: (Song) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(songs) { song ->
            SongItem(
                song = song,
                onClick = { onSongClick(song) },
                onEditClick = { onSongEdit(song) },
                onViewClick = { onSongView(song) },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun EmptySongsMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "No hay canciones",
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Crea tu primera canción",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
