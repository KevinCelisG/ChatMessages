package com.readyChatAI.readyChatMessages.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class Util {
    companion object {
        fun formattedDateText(instant: Instant): String {
            val zoneId = ZoneId.of("America/Bogota")
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
            val formattedDate = instant
                .atZone(zoneId)
                .format(formatter)
            return formattedDate
        }

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