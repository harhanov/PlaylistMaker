package com.practicum.playlistmaker.settings.data

import com.practicum.playlistmaker.settings.domain.ThemeSettings

interface SettingsLocalDataSource {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}