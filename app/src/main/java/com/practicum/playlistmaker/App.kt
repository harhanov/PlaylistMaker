package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.media_library.di.mediaLibraryDatabaseModule
import com.practicum.playlistmaker.media_library.di.mediaLibraryDomainModule
import com.practicum.playlistmaker.media_library.di.mediaLibraryUIModule
import com.practicum.playlistmaker.player.di.playerDataModule
import com.practicum.playlistmaker.player.di.playerInteractorModule
import com.practicum.playlistmaker.player.di.playerRepositoryModule
import com.practicum.playlistmaker.player.di.playerViewModelModule
import com.practicum.playlistmaker.search.di.localDataSourceModule
import com.practicum.playlistmaker.search.di.searchNetworkDataModule
import com.practicum.playlistmaker.search.di.searchUiModule
import com.practicum.playlistmaker.search.di.searchViewModelModule
import com.practicum.playlistmaker.search.di.tracksInteractorModule
import com.practicum.playlistmaker.search.di.tracksRepositoryModule
import com.practicum.playlistmaker.settings.data.SettingsLocalDataSource
import com.practicum.playlistmaker.settings.data.impl.SettingsLocalDataSourceImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.di.settingsDataModule
import com.practicum.playlistmaker.settings.di.settingsInteractorsModule
import com.practicum.playlistmaker.settings.di.settingsRepositoryModule
import com.practicum.playlistmaker.settings.di.settingsViewModelModule
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    private lateinit var settingsRepository: SettingsRepository
    private lateinit var settingsLocalDataSource: SettingsLocalDataSource

    override fun onCreate() {
        super.onCreate()
        settingsLocalDataSource = SettingsLocalDataSourceImpl(
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        )
        val settingsStorage =
            SettingsLocalDataSourceImpl(getSharedPreferences(PREFS_NAME, MODE_PRIVATE))
        settingsRepository = SettingsRepositoryImpl(settingsStorage)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val darkThemeEnabled = if (sharedPreferences.contains(DARK_THEME_KEY)) {
            settingsRepository.getThemeSettings().isNightModeEnabled
        } else {
            null
        }

        darkThemeEnabled?.let {
            switchTheme(darkThemeEnabled)
        }

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
            modules(
                mediaLibraryDatabaseModule,
                mediaLibraryDomainModule,
                mediaLibraryUIModule,
            )
        }
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        val nightMode = if (darkThemeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    companion object {
        private const val PREFS_NAME = "local_storage"
        private const val DARK_THEME_KEY = "dark_theme"
    }
}
