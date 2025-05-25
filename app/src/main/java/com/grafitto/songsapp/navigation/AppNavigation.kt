// navigation/AppNavigation.kt
package com.grafitto.songsapp.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepository
import com.grafitto.songsapp.ui.theme.SongsAPPTheme
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ConfigurationScreenWidthHeight")
@Suppress("ktlint:standard:function-naming", "detekt.FunctionNaming", "detekt.LongMethod", "detekt.MagicNumber")
@Composable
fun AppNavigation(repository: SongsRepository) {
    SongsAPPTheme(dynamicColor = false) {
        val navController = rememberNavController()
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp
        val isTablet = screenWidthDp >= 600

        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination?.route

        val categoryViewModelFromAppNavigation: CategoryViewModel? =
            if (currentRoute?.startsWith("category_edit") == true) {
                viewModel(
                    key = "category_edit_vm",
                    factory =
                        CategoryViewModelFactory(
                            LocalContext.current.let {
                                SongsDatabase.getDatabase(it).categoryDao()
                            },
                        ),
                )
            } else {
                null
            }

        val baseNavItems =
            listOf(
                Triple("main", Icons.Default.Home, "Inicio"),
                Triple("songs", Icons.Default.MusicNote, "Canciones"),
                Triple("categories", Icons.AutoMirrored.Filled.List, "Categorías"),
                Triple("repertoires", Icons.Default.LibraryMusic, "Repertorios"),
                Triple("settings", Icons.Default.Settings, "Configuración"),
            )

        val dynamicNavItems = getDynamicNavItems(navController, currentRoute, baseNavItems, categoryViewModelFromAppNavigation)

        Surface(color = MaterialTheme.colorScheme.surfaceContainerLow) {
            if (isTablet) {
                Row {
                    NavigationRail(
                        modifier = Modifier.shadow(4.dp),
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        header = {
                            Text("SongsApp", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.primary)
                        },
                    ) {
                        dynamicNavItems.forEach { item ->
                            NavigationRailItem(
                                selected = currentRoute == item.route,
                                onClick = item.onClick,
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) },
                                alwaysShowLabel = true,
                                colors =
                                    NavigationRailItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                    ),
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxSize()) {
                        Scaffold(
                            contentWindowInsets =
                                androidx.compose.foundation.layout
                                    .WindowInsets(0, 0, 0, 0),
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                        ) { innerPadding ->
                            // Llamada directa a la función importada
                            AppNavHost(navController, repository, Modifier.padding(innerPadding), categoryViewModelFromAppNavigation)
                        }
                    }
                }
            } else {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    contentWindowInsets =
                        androidx.compose.foundation.layout
                            .WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            tonalElevation = NavigationBarDefaults.Elevation,
                        ) {
                            dynamicNavItems.forEach { item ->
                                NavigationBarItem(
                                    selected = currentRoute == item.route,
                                    onClick = item.onClick,
                                    icon = { Icon(item.icon, contentDescription = item.label) },
                                    label = { Text(item.label) },
                                    alwaysShowLabel = true,
                                    colors =
                                        NavigationBarItemDefaults.colors(
                                            selectedIconColor = MaterialTheme.colorScheme.primary,
                                            selectedTextColor = MaterialTheme.colorScheme.primary,
                                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                        ),
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    // Llamada directa a la función importada
                    AppNavHost(navController, repository, Modifier.padding(innerPadding), categoryViewModelFromAppNavigation)
                }
            }
        }
    }
}
