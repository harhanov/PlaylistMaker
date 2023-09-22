package com.practicum.playlistmaker.search.di

import com.practicum.playlistmaker.search.data.TracksRepositoryImpl
import com.practicum.playlistmaker.search.domain.TracksRepository
import org.koin.dsl.module

val tracksRepositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(
            networkClient = get(),
            localStorage = get()
        )
    }
}