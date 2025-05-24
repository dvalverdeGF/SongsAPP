package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "songs",
    foreignKeys = [
        ForeignKey(
            entity = Author::class, // Asumiendo que tienes una entidad Author.kt
            parentColumns = ["id"],
            childColumns = ["author_id"],
            onDelete = ForeignKey.SET_NULL, // O ForeignKey.CASCADE según la lógica de negocio
        ),
    ],
    indices = [Index(value = ["author_id"])],
)
data class Song(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val name: String?,
    val reference: String?,
    @ColumnInfo(name = "author_id")
    val authorId: Long?, // Clave foránea a Author
    // La relación OneToOne con Lyric se maneja a través de una clave foránea (en Song o en Lyric)
    // La relación ManyToMany con Category se maneja a través de una tabla de unión (SongCategory)
)
