package com.readyChatAI.readyChatMessages.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.readyChatAI.readyChatMessages.presentation.screens.categories.CategoriesScreen
import com.readyChatAI.readyChatMessages.presentation.screens.messages.MessagesScreen
import com.readyChatAI.readyChatMessages.presentation.screens.settings.SettingsScreen
import com.readyChatAI.readyChatMessages.presentation.screens.splash.SplashScreen

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.SplashScreen,
        modifier = modifier
    ) {
        composable<Screen.SplashScreen> {
            SplashScreen(navHostController)
        }

        composable<Screen.MessagesScreen> {
            MessagesScreen(navHostController)
        }

        composable<Screen.CategoriesScreen> {
            CategoriesScreen(navHostController)
        }

        composable<Screen.SettingsScreen> {
            SettingsScreen(navHostController)
        }
    }
}