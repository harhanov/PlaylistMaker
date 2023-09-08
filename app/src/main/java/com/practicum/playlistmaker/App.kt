package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsLocalDataSourceImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository

class App : Application() {
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()

        val settingsStorage =
            SettingsLocalDataSourceImpl(getSharedPreferences("MyPrefs", MODE_PRIVATE))
        settingsRepository = SettingsRepositoryImpl(settingsStorage)

        switchTheme(
            settingsRepository.getThemeSettingsLiveData().value?.isNightModeEnabled ?: false
        )
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}