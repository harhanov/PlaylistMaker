package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.data.FavouritesRepositoryImpl
import com.practicum.playlistmaker.media_library.domain.FavouritesRepository
import com.practicum.playlistmaker.media_library.domain.db.FavouritesInteractor
import com.practicum.playlistmaker.media_library.domain.impl.FavouritesInteractorImpl
import org.koin.dsl.module

val favouritesDomainModule = module {
    single<FavouritesInteractor> {
        FavouritesInteractorImpl(get())
    }
    single<FavouritesRepository> { FavouritesRepositoryImpl(get(), get()) }
}
