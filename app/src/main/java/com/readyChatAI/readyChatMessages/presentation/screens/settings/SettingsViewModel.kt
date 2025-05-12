package com.readyChatAI.readyChatMessages.presentation.screens.settings

import android.app.Application
import android.content.res.Configuration
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.readyChatAI.readyChatMessages.domain.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val preferencesRepository: PreferencesRepository
) : AndroidViewModel(application) {

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private val _selectedLanguage = MutableStateFlow("es")
    val selectedLanguage: StateFlow<String> = _selectedLanguage

    private val _requiresActivityRecreate = MutableStateFlow(false)
    val requiresActivityRecreate: StateFlow<Boolean> = _requiresActivityRecreate

    fun initializePreferences() {
        viewModelScope.launch {
            val defaultSystemDarkMode = isSystemDarkModeEnabled()

            val userDarkTheme = preferencesRepository.loadThemePreference(defaultSystemDarkMode)
            _isDarkTheme.value = userDarkTheme

            val userLang = preferencesRepository.loadLanguagePreference("es")
            _selectedLanguage.value = userLang
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newTheme = !_isDarkTheme.value
            _isDarkTheme.value = newTheme
            preferencesRepository.saveThemePreference(newTheme)
        }
    }

    fun changeLanguage(languageCode: String) {
        viewModelScope.launch {
            _selectedLanguage.value = languageCode
            preferencesRepository.saveLanguagePreference(languageCode)
            _requiresActivityRecreate.value = true
        }
    }

    fun onRecreateDone() {
        _requiresActivityRecreate.value = false
    }

    private fun isSystemDarkModeEnabled(): Boolean {
        val uiMode = getApplication<Application>().resources.configuration.uiMode
        val nightModeFlags = uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }
}