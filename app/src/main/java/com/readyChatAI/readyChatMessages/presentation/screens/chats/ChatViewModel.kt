package com.readyChatAI.readyChatMessages.presentation.screens.chats

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readyChatAI.readyChatMessages.domain.repository.ChatRepository
import com.readyChatAI.readyChatMessages.util.Constants
import com.readyChatAI.readyChatMessages.util.Resource
import com.readyChatAI.readyChatMessages.util.Util
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    var state by mutableStateOf(ChatState())

    init {
        observeNewMessages()
    }

    fun onEvent(messageEvent: ChatEvent) {
        when (messageEvent) {
            is ChatEvent.GetChats -> {
                getChats()
            }

            is ChatEvent.SendMessage -> {
                sendMessage(message = messageEvent.message, phoneNumber = messageEvent.phoneNumber)
            }

            is ChatEvent.SelectChat -> {
                state = state.copy(selectedChat = messageEvent.chat)
            }
        }
    }

    private fun observeNewMessages() {
        viewModelScope.launch {
            chatRepository.areNewMessages.observeForever { areNewMessages ->
                if (areNewMessages) {
                    state = state.copy(newMessages = true)
                }
            }
        }
    }

    private fun getChats() {
        viewModelScope.launch {
            chatRepository.getChats().collect { resource ->
                state = when (resource) {
                    is Resource.Successful -> {
                        val newChats = resource.data ?: emptyList()
                        var currentChat = state.selectedChat

                        newChats.forEach {
                            if (
                                Util.advancedMatch(
                                    it.sender.phoneNumber,
                                    state.selectedChat?.sender?.phoneNumber.orEmpty()
                                )
                            ) {
                                currentChat = it
                            }
                        }

                        state.copy(
                            listChats = newChats,
                            selectedChat = currentChat,
                            newMessages = false
                        )
                    }

                    is Resource.Error -> {
                        state.copy(listChats = emptyList(), newMessages = false)
                    }

                    is Resource.Loading -> {
                        state.copy(isLoading = resource.isLoading)
                    }
                }
            }
        }
    }

    private fun sendMessage(message: String, phoneNumber: String) {
        viewModelScope.launch {
            chatRepository.sendSms(phoneNumber, message)
            /*.collect { resource ->
            state = when (resource) {
                is Resource.Successful -> {
                    state.copy(listChats = resource.data ?: emptyList())
                }

                is Resource.Error -> {
                    state.copy(listChats = emptyList())
                }

                is Resource.Loading -> {
                    state.copy(isLoading = resource.isLoading)
                }
            }
        }*/
        }
    }
}