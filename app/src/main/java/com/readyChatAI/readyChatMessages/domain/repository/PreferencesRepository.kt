package com.readyChatAI.readyChatMessages.domain.repository

interface PreferencesRepository {

    fun loadThemePreference(defaultSystemTheme: Boolean): Boolean

    fun saveThemePreference(isDarkTheme: Boolean)

    fun loadLanguagePreference(defaultLanguage: String): String

    fun saveLanguagePreference(languageCode: String)
}
