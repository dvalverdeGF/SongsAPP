// ui/components/VerseEditor.kt
package com.grafitto.songsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grafitto.songsapp.data.model.Verse

@Composable
fun VerseEditor(
    verse: Verse,
    onVerseChange: (Verse) -> Unit,
    onChordLineClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
    ) {
        // Línea de acordes (clickable para mostrar opciones futuras)
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp),
                    ).clickable { onChordLineClick() }
                    .padding(8.dp),
        ) {
            Text(
                text = verse.chords.ifEmpty { "Pulsa para añadir acordes" },
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color =
                            if (verse.chords.isEmpty()) {
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            },
                    ),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Línea de letra (editable)
        BasicTextField(
            value = verse.lyrics,
            onValueChange = { onVerseChange(verse.copy(lyrics = it)) },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(4.dp),
                    ).padding(8.dp),
            textStyle =
                MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            decorationBox = { innerTextField ->
                Box {
                    if (verse.lyrics.isEmpty()) {
                        Text(
                            text = "Escribe la letra aquí",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        )
                    }
                    innerTextField()
                }
            },
        )
    }
}
