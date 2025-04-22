package com.grafitto.songsapp.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grafitto.songsapp.data.model.Song

@Suppress("ktlint:standard:function-naming")
@Composable
fun SongItem(
    song: Song,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onViewClick: () -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(false) }

    val offsetX by animateDpAsState(
        targetValue = if (isExpanded) (-110).dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.7f),
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        // Fila con iconos (visible cuando la tarjeta está deslizada)
        Row(
            modifier =
                Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconButton(onClick = {
                onEditClick()
                isExpanded = false
            }) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar canción",
                )
            }

            IconButton(onClick = {
                onViewClick()
                isExpanded = false
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TextSnippet,
                    contentDescription = "Ver canción en modo texto",
                )
            }
        }

        // Tarjeta deslizable
        Card(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .offset(x = offsetX)
                    .clickable {
                        // Solo cambiamos el estado de expansión al hacer clic
                        isExpanded = !isExpanded
                    },
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
