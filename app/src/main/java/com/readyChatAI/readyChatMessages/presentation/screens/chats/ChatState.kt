package com.readyChatAI.readyChatMessages.presentation.screens.chats

import com.readyChatAI.readyChatMessages.domain.model.Chat

data class ChatState(
    val isLoading: Boolean = false,
    val listChats: List<Chat> = emptyList(),
    val selectedChat: Chat? = null,
    val newMessages: Boolean = false
)
