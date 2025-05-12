package com.readyChatAI.readyChatMessages.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.readyChatAI.readyChatMessages.data.local.entities.CategoryEntity

@Database(
    entities = [
        CategoryEntity::class
    ],
    version = 1
)
abstract class ReadyChatMessagesDatabase : RoomDatabase() {
    abstract val dao: ReadyChatMessagesDao
}