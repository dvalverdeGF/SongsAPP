package com.grafitto.songsapp.data.repository

import androidx.lifecycle.LiveData
import com.grafitto.songsapp.data.database.dao.AuthorDao
import com.grafitto.songsapp.data.database.entity.Author
import com.grafitto.songsapp.data.database.relation.AuthorWithSongs

class AuthorRepository(
    private val authorDao: AuthorDao,
) {
    // Asume que authorDao.getAllAuthors() devuelve LiveData<List<Author>>
    val allAuthors: LiveData<List<Author>> = authorDao.getAllAuthors()

    // Asume que authorDao.getAuthorWithSongsById(id) devuelve LiveData<AuthorWithSongs>
    fun getAuthorWithSongsById(authorId: Long): LiveData<AuthorWithSongs> = authorDao.getAuthorWithSongsById(authorId)

    suspend fun insert(author: Author) {
        authorDao.insert(author)
    }

    suspend fun update(author: Author) {
        authorDao.update(author)
    }

    suspend fun delete(author: Author) {
        authorDao.delete(author)
    }
}
