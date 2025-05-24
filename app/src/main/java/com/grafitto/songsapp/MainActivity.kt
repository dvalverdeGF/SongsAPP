package com.grafitto.songsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.grafitto.songsapp.navigation.AppNavigation
import com.grafitto.songsapp.ui.theme.SongsAPPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener repositorio desde la aplicación
        val repository = (application as SongsApplication).repository

        enableEdgeToEdge()
        setContent {
            SongsAPPTheme {
                AppNavigation(repository)
            }
        }
    }
}
