package com.readyChatAI.readyChatMessages.presentation.screens.messages

sealed class MessageEvent {
    data object GetMessages : MessageEvent()
}
