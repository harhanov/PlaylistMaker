package com.practicum.playlistmaker.settings.data.impl


import com.practicum.playlistmaker.settings.data.SettingsLocalDataSource
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.ThemeSettings

class SettingsRepositoryImpl(private val settingsLocalDataSource: SettingsLocalDataSource) :
    SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return settingsLocalDataSource.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsLocalDataSource.updateThemeSetting(settings)
    }
}



