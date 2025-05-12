package com.readyChatAI.readyChatMessages.domain.model

data class Chat(
    val sender: Sender,
    val listMessages: MutableList<Message>,
    var listCategories: MutableList<String> = emptyList<String>().toMutableList(),
)

data class Sender(
    val name: String?,
    val phoneNumber: String
)
