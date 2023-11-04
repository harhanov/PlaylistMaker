package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.ui.FavouritesTracksViewModel
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.search.ui.TrackListAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favouritesUIModule = module {
    factory { TrackListAdapter() }

    viewModel {
        FavouritesTracksViewModel(get())
    }
    single { TrackModel }
}
