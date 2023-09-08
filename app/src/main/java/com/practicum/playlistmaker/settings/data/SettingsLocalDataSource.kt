package com.practicum.playlistmaker.settings.data

import androidx.lifecycle.LiveData
import com.practicum.playlistmaker.settings.domain.ThemeSettings

interface SettingsLocalDataSource {
    fun updateThemeSetting(settings: ThemeSettings)
    fun getThemeSettingsLiveData(): LiveData<ThemeSettings>
}