package com.grafitto.songsapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Suppress("ktlint:standard:function-naming")
@Composable
fun TrailBar(
    showHome: Boolean = true,
    onHomeClick: (() -> Unit)? = null,
    sectionLabel: String? = null,
    onSectionClick: (() -> Unit)? = null,
    middleContent: (@Composable RowScope.() -> Unit)? = null,
    showBack: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    actionEnabled: Boolean = true,
    actionIcon: ImageVector? = null,
    settingsIcon: ImageVector? = null,
    onSettingsClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier.padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (showHome && onHomeClick != null) {
            IconButton(onClick = onHomeClick) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Inicio")
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (sectionLabel != null && onSectionClick != null) {
            Button(onClick = onSectionClick) {
                Text(sectionLabel)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        middleContent?.invoke(this)

        if (showBack && onBackClick != null) {
            if (middleContent != null || sectionLabel != null) {
                Spacer(modifier = Modifier.width(8.dp))
            }
            Button(onClick = onBackClick) {
                Text("Atrás")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        if (actionLabel != null && onActionClick != null) {
            Button(onClick = onActionClick, enabled = actionEnabled) {
                if (actionIcon != null) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                }
                Text(actionLabel)
            }
            if (settingsIcon != null && onSettingsClick != null) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }

        if (settingsIcon != null && onSettingsClick != null) {
            IconButton(onClick = onSettingsClick) {
                Icon(imageVector = settingsIcon, contentDescription = "Configuración")
            }
        }
    }
}
