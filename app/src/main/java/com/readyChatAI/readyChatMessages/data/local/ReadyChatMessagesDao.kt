package com.readyChatAI.readyChatMessages.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.readyChatAI.readyChatMessages.data.local.entities.CategoryEntity

@Dao
interface ReadyChatMessagesDao {

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM CategoryEntity WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCategory(category: CategoryEntity)

    @Query("SELECT * FROM CategoryEntity")
    suspend fun getCategories(): List<CategoryEntity>
}