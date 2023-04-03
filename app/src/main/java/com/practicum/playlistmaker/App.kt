package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

private var darkTheme = false

class App : Application() {
    override fun onCreate() {
        super.onCreate()
// Извлекаем сохраненные настройки из SharedPreferences
        val sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean("dark_theme", false)

        // Если сохраненных настроек нет, применяем текущую тему
        if (!sharedPrefs.contains("dark_theme")) {
            applyCurrentTheme()
        }
    }

    private fun applyCurrentTheme() {
        // Здесь мы можем определить, какую тему использовать в зависимости от устройства, конфигурации пользователя и т.д.
        // В данном случае мы просто устанавливаем светлую тему
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}