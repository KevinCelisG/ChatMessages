package com.readyChatAI.readyChatMessages.util

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class Util {
    companion object {
        private fun normalizeNumber(number: String): String {
            return number.replace("[^0-9]".toRegex(), "")
        }

        private fun matchNumbers(firstNumber: String, secondNumber: String): Boolean {
            val n1 = normalizeNumber(firstNumber)
            val n2 = normalizeNumber(secondNumber)

            if (n1 == n2) return true

            val (longer, shorter) = if (n1.length > n2.length) Pair(n1, n2) else Pair(n2, n1)

            val isSuffixMatch = longer.endsWith(shorter)

            val diff = longer.length - shorter.length

            return isSuffixMatch && diff in 1..3
        }

        fun advancedMatch(firstNumber: String, secondNumber: String): Boolean {
            val first = normalizeNumber(firstNumber)
            val second = normalizeNumber(secondNumber)

            return when {
                first == second -> true
                first.length == second.length -> false
                else -> {
                    val possibleVariations = listOf(
                        first to second,
                        second to first,
                        addCountryCodeVariations(first) to second,
                        first to addCountryCodeVariations(second)
                    )

                    possibleVariations.any { (a, b) -> matchNumbers(a, b) }
                }
            }
        }

        private fun addCountryCodeVariations(number: String): String {
            val commonCodes = listOf("1", "57", "52", "55", "34", "58")
            return commonCodes.joinToString("") { "$it$number" }
        }

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

        fun setLanguage(context: Context, languageCode: String) {
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            val resources = context.resources
            val config = Configuration(resources.configuration)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            context.createConfigurationContext(config)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}