package com.readyChatAI.readyChatMessages.presentation.screens.chats.load_chats

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.readyChatAI.readyChatMessages.R
import com.readyChatAI.readyChatMessages.data.util.getContactNameFromUri
import com.readyChatAI.readyChatMessages.data.util.getPhoneNumberFromUri
import com.readyChatAI.readyChatMessages.domain.model.Category
import com.readyChatAI.readyChatMessages.domain.model.Chat
import com.readyChatAI.readyChatMessages.domain.model.Message
import com.readyChatAI.readyChatMessages.domain.model.Sender
import com.readyChatAI.readyChatMessages.navigation.Screen
import com.readyChatAI.readyChatMessages.presentation.components.header.HeaderComponent
import com.readyChatAI.readyChatMessages.presentation.components.items.CategoryTypeItem
import com.readyChatAI.readyChatMessages.presentation.components.items.ChatItem
import com.readyChatAI.readyChatMessages.presentation.screens.categories.CategoryEvent
import com.readyChatAI.readyChatMessages.presentation.screens.categories.CategoryViewModel
import com.readyChatAI.readyChatMessages.presentation.screens.chats.ChatEvent
import com.readyChatAI.readyChatMessages.presentation.screens.chats.ChatViewModel
import com.readyChatAI.readyChatMessages.util.Constants
import com.readyChatAI.readyChatMessages.util.Util

@Composable
fun ChatsScreen(
    navHostController: NavHostController,
    chatViewModel: ChatViewModel,
    categoryViewModel: CategoryViewModel
) {
    val context = LocalContext.current

    val chatState = chatViewModel.state
    val categoryState = categoryViewModel.state

    var selectedCategory: String by rememberSaveable { mutableStateOf("All") }

    val pickContactLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickContact()
    ) { uri ->
        uri?.let { handleContactPicked(it, chatViewModel, navHostController, context) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderComponent(
            titleScreen = context.getString(R.string.chats_title),
            onClickListener = {
                pickContactLauncher.launch(null)
            }
        )

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                CategoryTypeItem(
                    category = Category(0L, "All", emptyList()),
                    isSelected = "All" == selectedCategory,
                    onClick = { category ->
                        selectedCategory = category.name
                    })
                Spacer(modifier = Modifier.width(10.dp))
            }

            items(categoryState.listCategories.size) {
                val currentCategory = categoryState.listCategories[it]
                CategoryTypeItem(
                    category = currentCategory,
                    isSelected = currentCategory.name == selectedCategory,
                    onClick = { category ->
                        selectedCategory = category.name
                    })
                Spacer(modifier = Modifier.width(10.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyColumn {
            items(chatState.listChats.size) {
                val currentChat = chatState.listChats[it]

                if (currentChat.listCategories.any { category -> category == selectedCategory } || selectedCategory == "All") {
                    ChatItem(
                        chat = currentChat,
                        onItemClick = {
                            chatViewModel.onEvent(ChatEvent.SelectChat(chat = currentChat))
                            navHostController.navigate(Screen.ChatScreen)
                        }
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            if (chatState.isLoading) {
                CircularProgressIndicator()
            }
        }
    }

    LaunchedEffect(chatState.newMessages) {
        if (chatState.newMessages) {
            chatViewModel.onEvent(ChatEvent.GetChats)
        }
    }

    LaunchedEffect(Unit) {
        if (categoryState.listCategories.isEmpty()) {
            categoryViewModel.onEvent(CategoryEvent.GetCategories)
        }

        if (chatState.listChats.isEmpty()) {
            chatViewModel.onEvent(ChatEvent.GetChats)
        }
    }

    BackHandler {
        (context as Activity).finish()
    }
}

private fun handleContactPicked(
    uri: Uri,
    viewModel: ChatViewModel,
    navHostController: NavHostController,
    context: Context
) {
    val phoneNumber =
        context.getPhoneNumberFromUri(uri)?.replace(" ", "")
    val contactName = context.getContactNameFromUri(uri)

    if (phoneNumber != null) {
        val existingChat =
            viewModel.state.listChats.find {
                Util.advancedMatch(
                    it.sender.phoneNumber,
                    phoneNumber
                )
            }

        if (existingChat != null) {
            viewModel.onEvent(ChatEvent.SelectChat(existingChat))
            navHostController.navigate(Screen.ChatScreen)
        } else {
            val newChat = Chat(
                sender = Sender(name = contactName, phoneNumber = phoneNumber),
                listMessages = emptyList<Message>().toMutableList()
            )

            viewModel.onEvent(ChatEvent.SelectChat(newChat))
            navHostController.navigate(Screen.ChatScreen)
        }
    }
}