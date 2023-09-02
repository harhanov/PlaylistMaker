package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.domain.ThemeSettings

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    private val themeSettingsLiveData: MutableLiveData<ThemeSettings> = MutableLiveData()

    private val sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == "dark_theme") {
            val isNightModeEnabled = sharedPreferences.getBoolean("dark_theme", false)
            themeSettingsLiveData.postValue(ThemeSettings(isNightModeEnabled))
        }
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefsListener)

        // Первоначальное обновление LiveData при создании репозитория
        val isNightModeEnabled = sharedPreferences.getBoolean("dark_theme", false)
        themeSettingsLiveData.postValue(ThemeSettings(isNightModeEnabled))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean("dark_theme", settings.isNightModeEnabled).apply()
        val nightMode = if (settings.isNightModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    override fun getThemeSettingsLiveData(): LiveData<ThemeSettings> {
        return themeSettingsLiveData
    }
}


