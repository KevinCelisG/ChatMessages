package com.readyChatAI.readyChatMessages.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val name: String) {

    @Serializable
    data object SplashScreen : Screen("SplashScreen")

    @Serializable
    data object ChatsScreen : Screen("ChatsScreen")

    @Serializable
    data object CategoriesScreen : Screen("CategoriesScreen")

    @Serializable
    data object SettingsScreen : Screen("SettingsScreen")

    @Serializable
    data object ChatScreen : Screen("ChatScreen")
}
