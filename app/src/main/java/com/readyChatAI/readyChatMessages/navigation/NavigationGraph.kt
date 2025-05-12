package com.readyChatAI.readyChatMessages.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.readyChatAI.readyChatMessages.presentation.screens.categories.CategoriesScreen
import com.readyChatAI.readyChatMessages.presentation.screens.categories.CategoryViewModel
import com.readyChatAI.readyChatMessages.presentation.screens.chats.ChatViewModel
import com.readyChatAI.readyChatMessages.presentation.screens.chats.chat.ChatScreen
import com.readyChatAI.readyChatMessages.presentation.screens.chats.load_chats.ChatsScreen
import com.readyChatAI.readyChatMessages.presentation.screens.settings.SettingsScreen
import com.readyChatAI.readyChatMessages.presentation.screens.settings.SettingsViewModel
import com.readyChatAI.readyChatMessages.presentation.screens.splash.SplashScreen

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    settingsViewModel: SettingsViewModel
) {
    val chatViewModel: ChatViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()

    NavHost(
        navController = navHostController,
        startDestination = Screen.SplashScreen,
        modifier = modifier
    ) {
        composable<Screen.SplashScreen> {
            SplashScreen(navHostController, chatViewModel, categoryViewModel)
        }

        composable<Screen.ChatsScreen> {
            ChatsScreen(navHostController, chatViewModel, categoryViewModel)
        }

        composable<Screen.CategoriesScreen> {
            CategoriesScreen(categoryViewModel)
        }

        composable<Screen.SettingsScreen> {
            SettingsScreen(settingsViewModel)
        }

        composable<Screen.ChatScreen> {
            ChatScreen(navHostController, chatViewModel)
        }
    }
}