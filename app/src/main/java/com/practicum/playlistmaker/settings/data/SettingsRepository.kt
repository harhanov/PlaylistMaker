package com.practicum.playlistmaker.settings.data

import androidx.lifecycle.LiveData
import com.practicum.playlistmaker.settings.domain.ThemeSettings

interface SettingsRepository {
    fun getThemeSettingsLiveData(): LiveData<ThemeSettings>
    fun updateThemeSetting(settings: ThemeSettings)
}