package com.readyChatAI.readyChatMessages.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val listKeywords: List<String>,
)