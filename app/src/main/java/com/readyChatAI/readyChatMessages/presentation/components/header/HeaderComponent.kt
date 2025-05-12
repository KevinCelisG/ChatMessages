package com.readyChatAI.readyChatMessages.presentation.components.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.readyChatAI.readyChatMessages.presentation.components.spacers.SpacerVertical
import com.readyChatAI.readyChatMessages.ui.theme.Dimens
import com.readyChatAI.readyChatMessages.util.Util

@Composable
fun HeaderComponent(
    titleScreen: String = "",
    onClickListener: () -> Unit = {},
    showAddButton: Boolean = true
) {
    SpacerVertical(0.02f)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(Util.heightPercent(percent = Dimens.HEADER_HEIGHT)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = titleScreen,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )

            if (showAddButton) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .clickable {
                            onClickListener()
                        }
                )
            }
        }
    }
}