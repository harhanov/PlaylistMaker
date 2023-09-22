package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {
    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }
}