package com.readyChatAI.readyChatMessages.domain.model

import java.time.Instant

data class Message(
    val text: String,
    val isSent: Boolean,
    val date: Instant
)
