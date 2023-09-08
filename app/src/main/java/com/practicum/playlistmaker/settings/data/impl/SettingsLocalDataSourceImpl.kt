package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.data.SettingsLocalDataSource
import com.practicum.playlistmaker.settings.domain.ThemeSettings

class SettingsLocalDataSourceImpl(private val sharedPreferences: SharedPreferences) :
    SettingsLocalDataSource {

    private val themeSettingsLiveData: MutableLiveData<ThemeSettings> = MutableLiveData()

    private val sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == KEY_DARK_THEME) {
            updateThemeLiveData()
        }
    }

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefsListener)
        updateThemeLiveData()
    }

    private fun updateThemeLiveData() {
        val isNightModeEnabled = sharedPreferences.getBoolean(KEY_DARK_THEME, false)
        themeSettingsLiveData.value = ThemeSettings(isNightModeEnabled)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putBoolean(KEY_DARK_THEME, settings.isNightModeEnabled).apply()
        val nightMode =
            if (settings.isNightModeEnabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    override fun getThemeSettingsLiveData(): LiveData<ThemeSettings> {
        return themeSettingsLiveData
    }

    companion object {
        private const val KEY_DARK_THEME = "dark_theme"
    }
}