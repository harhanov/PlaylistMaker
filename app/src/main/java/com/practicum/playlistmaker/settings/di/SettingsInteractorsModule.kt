package com.practicum.playlistmaker.settings.di

import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsInteractorsModule = module {

    single<SettingsInteractor> { SettingsInteractorImpl(get()) }
    single<SharingInteractor> { SharingInteractorImpl(get()) }
    single { ExternalNavigator(androidContext()) }

}