package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.ui.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistsUIModule = module {
    viewModel {
        PlaylistsViewModel()
    }
}