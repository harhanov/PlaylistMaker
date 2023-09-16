package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.data.SettingsLocalDataSource
import com.practicum.playlistmaker.settings.data.impl.SettingsLocalDataSourceImpl
import org.koin.dsl.module

val settingsDataModule = module {
    single<SettingsLocalDataSource> { SettingsLocalDataSourceImpl(get()) }
}