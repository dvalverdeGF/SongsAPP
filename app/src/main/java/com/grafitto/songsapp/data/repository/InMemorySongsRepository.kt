package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemorySongsRepository : SongsRepository {
    private val _songs = mutableListOf<Song>()
    private val _songsFlow = MutableStateFlow<List<Song>>(_songs.toList())

    override fun getAllSongs(): Flow<List<Song>> = _songsFlow.asStateFlow()

    override suspend fun getSongById(id: Int): Song? = _songs.find { it.id == id }

    override suspend fun addSong(song: Song) {
        val newId = (_songs.maxOfOrNull { it.id } ?: 0) + 1
        _songs.add(song.copy(id = newId))
        _songsFlow.value = _songs.toList()
    }

    override suspend fun updateSong(updatedSong: Song) {
        val index = _songs.indexOfFirst { it.id == updatedSong.id }
        if (index != -1) {
            _songs[index] = updatedSong
            _songsFlow.value = _songs.toList()
        }
    }

    override suspend fun clearSongs() {
        _songs.clear()
        _songsFlow.value = emptyList()
    }
}
