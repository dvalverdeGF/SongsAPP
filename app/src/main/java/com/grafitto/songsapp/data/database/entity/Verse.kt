package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "verses",
    foreignKeys = [
        ForeignKey(
            entity = Lyric::class,
            parentColumns = ["id"],
            childColumns = ["lyric_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["lyric_id"])],
)
data class Verse(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    @ColumnInfo(name = "lyric_id")
    val lyricId: Long, // Clave foránea a Lyric
    val text: String?, // Contenido del verso
    // Posición del verso en la letra
    @ColumnInfo(name = "order_in_lyric")
    val orderInLyric: Int? = null,
    // Las relaciones OneToMany con Annotation y LyricChord se manejan con claves foráneas en esas tablas
)
