package com.readyChatAI.readyChatMessages.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.readyChatAI.readyChatMessages.R
import com.readyChatAI.readyChatMessages.navigation.Screen
import com.readyChatAI.readyChatMessages.presentation.screens.categories.CategoryEvent
import com.readyChatAI.readyChatMessages.presentation.screens.categories.CategoryViewModel
import com.readyChatAI.readyChatMessages.presentation.screens.chats.ChatEvent
import com.readyChatAI.readyChatMessages.presentation.screens.chats.ChatViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navHostController: NavHostController,
    chatViewModel: ChatViewModel,
    categoryViewModel: CategoryViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 32.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.SemiBold
        )
    }

    LaunchedEffect(Unit) {
        categoryViewModel.onEvent(CategoryEvent.GetCategories)
        chatViewModel.onEvent(ChatEvent.GetChats)
        delay(2000)
        navHostController.navigate(Screen.ChatsScreen)
    }
}