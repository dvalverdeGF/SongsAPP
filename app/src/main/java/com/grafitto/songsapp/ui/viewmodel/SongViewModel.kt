package com.grafitto.songsapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.grafitto.songsapp.data.database.SongsDatabase
import com.grafitto.songsapp.data.database.dao.SongDao
import com.grafitto.songsapp.data.database.entity.Song
import com.grafitto.songsapp.data.database.relation.SongComplete
import kotlinx.coroutines.launch

class SongViewModel(
    application: Application,
) : AndroidViewModel(application) {
    private val songDao: SongDao

    // Ejemplo: LiveData para una lista de todas las canciones (entidad básica)
    // Podrías necesitar un método específico en tu SongDao para esto, ej: getAllSongs()
    val allSongs: LiveData<List<Song>>

    init {
        songDao = SongsDatabase.getDatabase(application).songDao()
        // Asumiendo que songDao.getAllSongs() devuelve LiveData<List<Song>>
        allSongs = songDao.getAllSongs() // Necesitarás este método en SongDao
    }

    // Ejemplo: Función para obtener una canción específica con todos sus detalles
    // Asumiendo que songDao.getSongCompleteById(id) devuelve LiveData<SongComplete>
    fun getSongCompleteById(songId: Long): LiveData<SongComplete> {
        return songDao.getSongCompleteById(songId) // Necesitarás este método en SongDao
    }

    // Ejemplo: Función para insertar una canción (si es necesario desde el ViewModel)
    fun insertSong(song: Song) =
        viewModelScope.launch {
            songDao.insert(song) // Asumiendo que tienes un método insert en SongDao
        }

    // Agrega aquí otros métodos que necesites para interactuar con SongDao
    // como update, delete, etc.
}
