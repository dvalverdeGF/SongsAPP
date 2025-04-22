package com.grafitto.songsapp.data.repository

import com.grafitto.songsapp.data.database.dao.SongDao
import com.grafitto.songsapp.data.database.dao.VerseDao
import com.grafitto.songsapp.data.database.entity.SongEntity
import com.grafitto.songsapp.data.database.entity.VerseEntity
import com.grafitto.songsapp.data.database.relation.SongWithVerses
import com.grafitto.songsapp.data.model.Song
import com.grafitto.songsapp.data.model.Verse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SongsRepositoryImpl(
    private val songDao: SongDao,
    private val verseDao: VerseDao,
) : SongsRepository {
    override fun getAllSongs(): Flow<List<Song>> =
        songDao.getAllSongs().map { entities ->
            entities.map { it.toModel() }
        }

    override suspend fun getSongById(id: Int): Song? = songDao.getSongWithVerses(id)?.toModel()

    suspend fun getVersesBySongId(songId: Int): List<VerseEntity> = verseDao.getVersesBySongId(songId)

    override suspend fun addSong(song: Song) {
        val songId = songDao.insertSong(song.toEntity()).toInt()

        val versesWithSongId =
            song.verses.mapIndexed { index, verse ->
                verse.toEntity(songId, index)
            }
        verseDao.insertVerses(versesWithSongId)
    }

    override suspend fun updateSong(song: Song) {
        songDao.updateSong(song.toEntity())
        // Añadir este método a VerseDao:
        // @Query("DELETE FROM verses WHERE songId = :songId")
        // suspend fun deleteVersesBySongId(songId: Int)
        verseDao.deleteVersesBySongId(song.id)

        val versesWithSongId =
            song.verses.mapIndexed { index, verse ->
                verse.toEntity(song.id, index)
            }
        verseDao.insertVerses(versesWithSongId)
    }

    override suspend fun clearSongs() {
        // Implementar la limpieza de canciones
        // @Query("DELETE FROM songs")
        // suspend fun deleteAllSongs()
        // songDao.deleteAllSongs()

        // @Query("DELETE FROM verses")
        // suspend fun deleteAllVerses()
        // verseDao.deleteAllVerses()
    }

    // Métodos de extensión para convertir entre entidades y modelos
    private fun SongEntity.toModel(): Song = Song(id, title, artist)

    private fun Song.toEntity(): SongEntity = SongEntity(id, title, artist)

    private fun Verse.toEntity(
        songId: Int,
        position: Int,
    ): VerseEntity = VerseEntity(0, songId, chords, lyrics, position)

    private fun SongWithVerses.toModel(): Song = Song(song.id, song.title, song.artist, verses.map { Verse(it.chords, it.lyrics) })
}
