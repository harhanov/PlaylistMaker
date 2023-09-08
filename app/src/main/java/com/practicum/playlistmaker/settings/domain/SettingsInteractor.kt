package com.practicum.playlistmaker.settings.domain

import androidx.lifecycle.LiveData

interface SettingsInteractor {
    fun getThemeSettingsLiveData(): LiveData<ThemeSettings>
    fun updateThemeSetting(settings: ThemeSettings)
}