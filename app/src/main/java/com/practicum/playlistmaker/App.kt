package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

private var darkTheme = false

class App : Application() {
    companion object {
        lateinit var sharedPrefs: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
// Извлекаем сохраненные настройки из SharedPreferences
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean("dark_theme", false)
        switchTheme(darkTheme)
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