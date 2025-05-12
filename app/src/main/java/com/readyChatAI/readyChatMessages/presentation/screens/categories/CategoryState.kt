package com.readyChatAI.readyChatMessages.presentation.screens.categories

import com.readyChatAI.readyChatMessages.domain.model.Category

data class CategoryState(
    val isLoading: Boolean = false,
    val listCategories: List<Category> = emptyList(),
    val newCategories: Boolean = false
)
