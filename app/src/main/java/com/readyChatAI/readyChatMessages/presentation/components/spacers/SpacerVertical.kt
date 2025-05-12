package com.readyChatAI.readyChatMessages.presentation.components.spacers

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.readyChatAI.readyChatMessages.util.Util

@Composable
fun SpacerVertical(percent: Float = 0.055f) {
    Spacer(
        modifier = Modifier.height(
            Util.heightPercent(percent = percent)
        )
    )
}