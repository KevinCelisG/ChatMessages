package com.readyChatAI.readyChatMessages.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.readyChatAI.readyChatMessages.domain.repository.PreferencesRepository
import com.readyChatAI.readyChatMessages.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesRepository {

    private val themePrefs: SharedPreferences =
        context.getSharedPreferences(Constants.THEME_PREFS, Context.MODE_PRIVATE)

    private val languagePrefs: SharedPreferences =
        context.getSharedPreferences(Constants.LANGUAGE_PREFS, Context.MODE_PRIVATE)

    override fun loadThemePreference(defaultSystemTheme: Boolean): Boolean {
        return themePrefs.getBoolean(Constants.IS_DARK_THEME, defaultSystemTheme)
    }

    override fun saveThemePreference(isDarkTheme: Boolean) {
        themePrefs.edit()
            .putBoolean(Constants.IS_DARK_THEME, isDarkTheme)
            .apply()
    }

    override fun loadLanguagePreference(defaultLanguage: String): String {
        return languagePrefs.getString(Constants.SELECTED_LANGUAGE, defaultLanguage)
            ?: defaultLanguage
    }

    override fun saveLanguagePreference(languageCode: String) {
        languagePrefs.edit()
            .putString(Constants.SELECTED_LANGUAGE, languageCode)
            .apply()
    }
}