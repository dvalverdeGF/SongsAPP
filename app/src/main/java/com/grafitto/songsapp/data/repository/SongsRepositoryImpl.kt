package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.database.dao.CategoryDao
import com.grafitto.songsapp.data.database.dao.SongDao
import com.grafitto.songsapp.data.database.dao.VerseDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.grafitto.songsapp.data.database.entity.Song as SongEntity
import com.grafitto.songsapp.data.model.Song as DomainSong

// Helper para mapear SongEntity a DomainSong
fun SongEntity.toDomainSong(): DomainSong =
    DomainSong(
        id = this.id?.toInt() ?: 0,
        name = this.name ?: "Nombre Desconocido",
        reference = this.reference,
        author = null, // Placeholder, necesita lógica de mapeo si es necesario
        categories = emptyList(), // Placeholder
        lyric = null, // Placeholder
    )

// Helper para mapear DomainSong a SongEntity
fun DomainSong.toSongEntity(): SongEntity =
    SongEntity(
        id = if (this.id == 0) null else this.id.toLong(),
        name = this.name,
        reference = this.reference,
        authorId = null, // Placeholder, necesita lógica de mapeo si es necesario
    )

class SongsRepositoryImpl(
    private val songDao: SongDao,
    private val verseDao: VerseDao,
    private val categoryDao: CategoryDao,
) : SongsRepository {
    override fun getAllSongs(): Flow<List<DomainSong>> =
        songDao.getAllSongs().map { list ->
            // list es Flow<List<SongEntity>>
            list.map { entity -> entity.toDomainSong() } // Mapear cada SongEntity a DomainSong
        }

    override suspend fun getSongById(songId: Int): DomainSong? {
        val songWithVerses = songDao.getSongWithVerses(songId)
        return songWithVerses?.song?.toDomainSong()
    }

    override suspend fun addSong(song: DomainSong) {
        songDao.insertSong(song.toSongEntity())
    }

    override suspend fun updateSong(song: DomainSong) {
        songDao.updateSong(song.toSongEntity())
    }

    override suspend fun deleteSong(song: DomainSong) {
        // TODO: Implementar deleteSong en SongDao y llamarlo aquí.
        // Por ejemplo: songDao.deleteSongById(song.id)
    }
}
