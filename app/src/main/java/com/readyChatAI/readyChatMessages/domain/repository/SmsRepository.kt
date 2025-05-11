package com.readyChatAI.readyChatMessages.domain.repository

import com.readyChatAI.readyChatMessages.domain.model.Message
import com.readyChatAI.readyChatMessages.util.Resource
import kotlinx.coroutines.flow.Flow

interface SmsRepository {

    fun sendSms(phoneNumber: String, message: String)

    fun getMessages(): Flow<Resource<List<Message>>>
}