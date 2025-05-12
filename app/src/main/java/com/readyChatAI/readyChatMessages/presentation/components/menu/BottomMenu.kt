package com.readyChatAI.readyChatMessages.presentation.components.menu

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.readyChatAI.readyChatMessages.R
import com.readyChatAI.readyChatMessages.navigation.Screen
import com.readyChatAI.readyChatMessages.ui.theme.Dimens
import com.readyChatAI.readyChatMessages.util.Util

@Composable
fun BottomMenu(
    navHostController: NavHostController,
    currentScreen: String
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(Util.heightPercent(percent = Dimens.BOTTOM_MENU_HEIGHT))
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.ChatsScreen.name,
            onClick = {
                navHostController.navigate(Screen.ChatsScreen)
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Message,
                    contentDescription = stringResource(id = R.string.chats_title),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.chats_title),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.CategoriesScreen.name,
            onClick = {
                navHostController.navigate(Screen.CategoriesScreen)
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Label,
                    contentDescription = stringResource(id = R.string.categories_title),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.categories_title),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        )

        NavigationBarItem(
            selected = currentScreen == Screen.SettingsScreen.name,
            onClick = {
                navHostController.navigate(Screen.SettingsScreen)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings_title),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            },
            label = {
                Text(
                    text = stringResource(id = R.string.settings_title),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        )
    }
}