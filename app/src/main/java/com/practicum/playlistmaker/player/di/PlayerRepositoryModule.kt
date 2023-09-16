package com.practicum.playlistmaker.player.di

import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.player.data.PlayerRepositoryImpl
import org.koin.dsl.module

val playerRepositoryModule = module {
    factory<PlayerRepository> {
        PlayerRepositoryImpl(playerClient = get())
    }
}