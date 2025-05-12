package com.readyChatAI.readyChatMessages.presentation.screens.categories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readyChatAI.readyChatMessages.domain.model.Category
import com.readyChatAI.readyChatMessages.domain.repository.ChatRepository
import com.readyChatAI.readyChatMessages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    var state by mutableStateOf(CategoryState())

    fun onEvent(categoryEvent: CategoryEvent) {
        when (categoryEvent) {
            is CategoryEvent.CreateCategory -> {
                createCategory(categoryEvent.category)
            }

            is CategoryEvent.GetCategories -> {
                getCategories()
            }

            is CategoryEvent.DeleteCategory -> {
                deleteCategory(categoryEvent.categoryId)
            }

            is CategoryEvent.UpdateCategory -> {
                updateCategory(categoryEvent.category)
            }
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            chatRepository.getCategories().collect { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        state.copy(
                            listCategories = resource.data ?: emptyList(),
                            newCategories = false
                        )
                    }

                    is Resource.Error -> {
                        state.copy(listCategories = emptyList(), newCategories = false)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun updateCategory(category: Category) {
        viewModelScope.launch {
            chatRepository.updateCategory(category).collect { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        state.copy(newCategories = true)
                    }

                    is Resource.Error -> {
                        state.copy(newCategories = false)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            chatRepository.deleteCategory(categoryId).collect { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        state.copy(newCategories = true)
                    }

                    is Resource.Error -> {
                        state.copy(newCategories = false)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun createCategory(category: Category) {
        viewModelScope.launch {
            chatRepository.createCategory(category).collect { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        state.copy(newCategories = true)
                    }

                    is Resource.Error -> {
                        state.copy(newCategories = false)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }
}