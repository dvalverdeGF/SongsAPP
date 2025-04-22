package com.grafitto.songsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepositoryImpl
import com.grafitto.songsapp.navigation.AppNavigation
import com.grafitto.songsapp.ui.theme.SongsAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener repositorio desde la aplicación o crear uno nuevo
        val repository =
            if (application is SongsApplication) {
                (application as SongsApplication).repository
            } else {
                val database = SongsDatabase.getDatabase(this)
                SongsRepositoryImpl(database.songDao(), database.verseDao())
            }

        enableEdgeToEdge()
        setContent {
            SongsAPPTheme {
                AppNavigation(repository)
            }
        }
    }
}
