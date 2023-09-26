package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.ui.FavTracksViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favTracksViewModelModule = module {
    viewModel {
        FavTracksViewModel(get())
    }
}