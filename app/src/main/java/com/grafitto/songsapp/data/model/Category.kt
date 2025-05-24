package com.grafitto.songsapp.data.model

data class Category(
    val id: Int = 0,
    val name: String,
    val parentId: Int? = null,
    val children: List<Category> = emptyList(),
)
