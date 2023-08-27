package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

private var darkTheme = false

class App : Application() {
    lateinit var sharingInteractor: SharingInteractor
    override fun onCreate() {
        super.onCreate()
// Извлекаем сохраненные настройки из SharedPreferences
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean("dark_theme", false)
        switchTheme(darkTheme)
        sharingInteractor = SharingInteractorImpl(ExternalNavigator(this))

    }

    fun getSettingsRepository(): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(sharedPrefs)
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

    companion object {
        lateinit var sharedPrefs: SharedPreferences
    }

}