package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.domain.db.FavouritesInteractor
import com.practicum.playlistmaker.media_library.domain.impl.FavouritesInteractorImpl
import org.koin.dsl.module

val favouritesInteractorModule = module {
    single<FavouritesInteractor>{
        FavouritesInteractorImpl(get())
    }
}