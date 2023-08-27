package com.practicum.playlistmaker.settings.domain

import androidx.lifecycle.LiveData

interface SettingsRepository {
    fun getThemeSettingsLiveData(): LiveData<ThemeSettings>
    fun updateThemeSetting(settings: ThemeSettings)
}