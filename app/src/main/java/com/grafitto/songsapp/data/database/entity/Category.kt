package com.grafitto.songsapp.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    // La relación ManyToMany con Song se maneja a través de una tabla de unión (por ejemplo, SongCategory)
)
