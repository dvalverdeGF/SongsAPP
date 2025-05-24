package com.grafitto.songsapp

import android.app.Application
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.repository.SongsRepository // Importación actualizada
import com.grafitto.songsapp.data.repository.SongsRepositoryImpl // Importación actualizada

class SongsApplication : Application() {
    private val database by lazy { SongsDatabase.getDatabase(this) }
    val repository: SongsRepository by lazy {
        SongsRepositoryImpl(database.songDao(), database.verseDao(), database.categoryDao())
    }
}
