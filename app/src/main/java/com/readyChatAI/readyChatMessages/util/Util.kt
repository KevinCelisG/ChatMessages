package com.readyChatAI.readyChatMessages.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Util {
    companion object {
        @Composable
        fun heightPercent(percent: Float): Dp {
            val configuration = LocalConfiguration.current
            val screenHeight = configuration.screenHeightDp
            return (screenHeight * percent).dp
        }

        @Composable
        fun widthPercent(percent: Float): Dp {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp
            return (screenWidth * percent).dp
        }
    }
}