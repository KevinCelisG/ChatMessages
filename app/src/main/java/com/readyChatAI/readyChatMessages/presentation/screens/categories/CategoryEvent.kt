package com.readyChatAI.readyChatMessages.presentation.screens.categories

import com.readyChatAI.readyChatMessages.domain.model.Category

sealed class CategoryEvent {
    data object GetCategories : CategoryEvent()
    data class CreateCategory(val category: Category) : CategoryEvent()
    data class DeleteCategory(val categoryId: Long) : CategoryEvent()
    data class UpdateCategory(val category: Category) : CategoryEvent()
}
