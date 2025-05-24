package com.grafitto.songsapp.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["parent_id"],
            onDelete = ForeignKey.SET_NULL, // O ForeignKey.CASCADE según tu lógica de negocio
        ),
    ],
    indices = [Index(value = ["parent_id"])],
)
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    @ColumnInfo(name = "parent_id")
    val parentId: Long? = null, // Columna para la relación jerárquica
    // La relación ManyToMany con Song se maneja a través de una tabla de unión (por ejemplo, SongCategory)
)
