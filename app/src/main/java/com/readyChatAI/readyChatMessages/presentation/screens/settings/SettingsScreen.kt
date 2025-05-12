package com.readyChatAI.readyChatMessages.presentation.screens.settings

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.readyChatAI.readyChatMessages.R
import com.readyChatAI.readyChatMessages.presentation.components.header.HeaderComponent
import com.readyChatAI.readyChatMessages.util.Util

@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel,
) {
    val context = LocalContext.current

    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
    val selectedLanguage by settingsViewModel.selectedLanguage.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        HeaderComponent(
            titleScreen = context.getString(R.string.settings_title),
            showAddButton = false
        )

        HorizontalDivider(
            modifier = Modifier
                .height(10.dp),
            color = MaterialTheme.colorScheme.onBackground
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Util.heightPercent(percent = 0.05f))
                    .clickable {
                        val newLanguage =
                            if (settingsViewModel.selectedLanguage.value == "es") "en" else "es"
                        settingsViewModel.changeLanguage(newLanguage)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Language",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(Util.widthPercent(percent = 0.02f)))

                Text(
                    text = stringResource(id = if (selectedLanguage == "es") R.string.set_to_english else R.string.set_to_spanish),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(Util.heightPercent(percent = 0.01f)))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Util.heightPercent(percent = 0.05f))
                    .clickable {
                        settingsViewModel.toggleTheme()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Theme",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(Util.widthPercent(percent = 0.02f)))

                Text(
                    text = stringResource(id = if (isDarkTheme) R.string.set_light else R.string.set_dark),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }

    BackHandler {
        (context as Activity).finish()
    }
}