package com.readyChatAI.readyChatMessages.presentation.screens.messages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readyChatAI.readyChatMessages.domain.repository.SmsRepository
import com.readyChatAI.readyChatMessages.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val smsRepository: SmsRepository
) : ViewModel() {

    var state by mutableStateOf(MessageState())

    fun onEvent(messageEvent: MessageEvent) {
        when (messageEvent) {
            is MessageEvent.GetMessages -> {
                getMessages()
            }
        }
    }

    private fun getMessages() {
        viewModelScope.launch {
            smsRepository.getMessages().collect { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        state.copy(listMessages = resource.data ?: emptyList())
                    }

                    is Resource.Error -> {
                        state.copy(listMessages = emptyList())
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }
}