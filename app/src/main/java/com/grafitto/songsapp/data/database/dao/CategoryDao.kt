package com.grafitto.songsapp.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.grafitto.songsapp.data.database.entity.Category
import com.grafitto.songsapp.data.database.relation.CategoryWithSongs

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories ORDER BY name ASC")
    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE parent_id IS NULL ORDER BY name ASC")
    fun getRootCategories(): LiveData<List<Category>> // quitar suspend

    @Query("SELECT * FROM categories WHERE parent_id = :categoryId ORDER BY name ASC")
    suspend fun getChildCategories(categoryId: Int): List<Category>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category): Long

    @Update
    suspend fun updateCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Int)

    @Transaction
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    fun getCategoryWithSongsById(categoryId: Long): LiveData<CategoryWithSongs>

    @Query("SELECT category_id, COUNT(song_id) as count FROM song_categories GROUP BY category_id")
    fun getSongsCountByCategory(): LiveData<
        Map<
            @MapColumn("category_id")
            Long,
            @MapColumn("count")
            Int,
        >,
    >
}
