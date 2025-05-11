package com.readyChatAI.readyChatMessages.presentation.screens.messages

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.readyChatAI.readyChatMessages.presentation.components.items.MessageItem

@Composable
fun MessagesScreen(
    navHostController: NavHostController,
    messageViewModel: MessageViewModel
) {
    val context = LocalContext.current

    val messageState = messageViewModel.state

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messageState.listMessages.size) { i ->
                MessageItem(messageState.listMessages[i])
            }
        }
    }

    LaunchedEffect(Unit) {
        messageViewModel.onEvent(MessageEvent.GetMessages)
    }

    BackHandler {
        (context as Activity).finish()
    }
}
