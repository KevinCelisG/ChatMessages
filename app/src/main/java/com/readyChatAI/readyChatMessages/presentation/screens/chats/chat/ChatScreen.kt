package com.readyChatAI.readyChatMessages.presentation.screens.chats.chat

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.readyChatAI.readyChatMessages.R
import com.readyChatAI.readyChatMessages.domain.model.Chat
import com.readyChatAI.readyChatMessages.domain.model.Message
import com.readyChatAI.readyChatMessages.presentation.screens.chats.ChatEvent
import com.readyChatAI.readyChatMessages.presentation.screens.chats.ChatViewModel
import com.readyChatAI.readyChatMessages.util.Constants
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatScreen(
    navHostController: NavHostController,
    chatViewModel: ChatViewModel
) {
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    var newMessage: String by remember { mutableStateOf("") }

    val chatState = chatViewModel.state
    var currentChat: Chat? by remember { mutableStateOf(chatState.selectedChat) }

    Scaffold(
        topBar = {
            ChatHeader(
                chat = currentChat,
                onBack = { navHostController.popBackStack() },
                context = context
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    state = scrollState,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    reverseLayout = true
                ) {
                    currentChat?.listMessages?.let { messages ->
                        val sortedMessages = messages.sortedByDescending { it.date }
                        items(sortedMessages) { message ->
                            MessageBubble(
                                message = message,
                                isUserMessage = message.isSent
                            )
                        }
                    }
                }

                MessageInputField(
                    message = newMessage,
                    onMessageChange = { newMessage = it },
                    onSend = {
                        if (newMessage.isNotBlank() && (currentChat != null)) {
                            chatViewModel.onEvent(
                                ChatEvent.SendMessage(
                                    message = newMessage,
                                    phoneNumber = currentChat?.sender?.phoneNumber.orEmpty()
                                )
                            )
                            newMessage = ""
                        }
                    },
                    context = context,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        }
    )

    LaunchedEffect(chatState.newMessages) {
        if (chatState.newMessages) {
            chatViewModel.onEvent(ChatEvent.GetChats)
        }
    }

    LaunchedEffect(chatState.selectedChat) {
        currentChat = chatState.selectedChat
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatHeader(
    chat: Chat?,
    onBack: () -> Unit,
    context: Context
) {
    val contactName = chat?.sender?.name ?: chat?.sender?.phoneNumber.orEmpty()

    TopAppBar(
        title = {
            Column {
                Text(
                    text = contactName,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = context.getString(R.string.back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun MessageBubble(
    message: Message,
    isUserMessage: Boolean
) {
    val alignment = if (isUserMessage) Alignment.End else Alignment.Start
    val bubbleColor = if (isUserMessage) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val time = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault())
            .format(Date(message.date.toEpochMilli()))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = alignment
    ) {
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp,
                        bottomStart = if (isUserMessage) 12.dp else 0.dp,
                        bottomEnd = if (isUserMessage) 0.dp else 12.dp
                    )
                )
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            Column {
                Text(
                    text = message.text,
                    color = if (isUserMessage) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = time.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isUserMessage) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.6f
                    )
                )
            }
        }
    }
}

@Composable
private fun MessageInputField(
    message: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = message,
            onValueChange = onMessageChange,
            placeholder = { Text(context.getString(R.string.write_message)) },
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = onSend,
            enabled = message.isNotBlank(),
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "Enviar mensaje",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}