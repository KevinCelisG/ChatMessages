package com.readyChatAI.readyChatMessages.domain.repository

import androidx.lifecycle.LiveData
import com.readyChatAI.readyChatMessages.domain.model.Category
import com.readyChatAI.readyChatMessages.domain.model.Chat
import com.readyChatAI.readyChatMessages.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatRepository {

    val areNewMessages: LiveData<Boolean>

    fun sendSms(phoneNumber: String, message: String)

    suspend fun getChats(): Flow<Resource<List<Chat>>>

    suspend fun informAboutNewMessages(value: Boolean = true)

    suspend fun createCategory(category: Category): Flow<Resource<Category>>

    suspend fun getCategories(): Flow<Resource<List<Category>>>

    suspend fun updateCategory(category: Category): Flow<Resource<Category>>

    suspend fun deleteCategory(categoryId: Long): Flow<Resource<Long>>
}