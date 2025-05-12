package com.readyChatAI.readyChatMessages.presentation.screens.chats

import com.readyChatAI.readyChatMessages.domain.model.Chat

sealed class ChatEvent {
    data object GetChats : ChatEvent()
    data class SendMessage(val message: String, val phoneNumber: String) : ChatEvent()
    data class SelectChat(val chat: Chat) : ChatEvent()
}
