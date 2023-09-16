package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.player.di.playerDataModule
import com.practicum.playlistmaker.player.di.playerInteractorModule
import com.practicum.playlistmaker.player.di.playerRepositoryModule
import com.practicum.playlistmaker.player.di.playerViewModelModule
import com.practicum.playlistmaker.search.di.*
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsLocalDataSourceImpl
import com.practicum.playlistmaker.settings.di.settingsDataModule
import com.practicum.playlistmaker.settings.di.settingsInteractorsModule
import com.practicum.playlistmaker.settings.di.settingsRepositoryModule
import com.practicum.playlistmaker.settings.di.settingsViewModelModule
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()

        val settingsStorage =
            SettingsLocalDataSourceImpl(getSharedPreferences("local_storage", MODE_PRIVATE))
        settingsRepository = SettingsRepositoryImpl(settingsStorage)

        switchTheme(
            settingsRepository.getThemeSettings().isNightModeEnabled
        )
        startKoin {
            androidContext(this@App)
            modules(
                playerDataModule,
                playerRepositoryModule,
                playerInteractorModule,
                playerViewModelModule,
            )
            modules(
                localDataSourceModule,
                searchNetworkDataModule,
                tracksInteractorModule,
                tracksRepositoryModule,
                searchUiModule,
                searchViewModelModule,
            )
            modules(
                settingsDataModule,
                settingsInteractorsModule,
                settingsRepositoryModule,
                settingsViewModelModule,
            )
        }
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}