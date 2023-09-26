package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.ui.FavouritesTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouritesTracksViewModelModule = module {
    viewModel {
        FavouritesTracksViewModel(get())
    }
}