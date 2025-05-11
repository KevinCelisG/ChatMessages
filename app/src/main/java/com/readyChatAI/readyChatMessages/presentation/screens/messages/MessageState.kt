package com.readyChatAI.readyChatMessages.presentation.screens.messages

import com.readyChatAI.readyChatMessages.domain.model.Message

data class MessageState(
    val isLoading: Boolean = false,
    val listMessages: List<Message> = emptyList()
)
