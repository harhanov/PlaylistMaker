package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.data.FavouritesRepositoryImpl
import com.practicum.playlistmaker.media_library.domain.FavouritesRepository
import org.koin.dsl.module

val favouritesRepositoryModule = module {
    single<FavouritesRepository> { FavouritesRepositoryImpl(get(), get()) }
}