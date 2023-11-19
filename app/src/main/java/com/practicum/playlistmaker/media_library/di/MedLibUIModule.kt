package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.playlists.ui.NewPlaylistViewModel
import com.practicum.playlistmaker.media_library.favourites.ui.FavouritesTracksViewModel
import com.practicum.playlistmaker.media_library.playlists.ui.PlaylistAdapter
import com.practicum.playlistmaker.media_library.playlists.ui.PlaylistsViewModel
import com.practicum.playlistmaker.player.domain.TrackModel
import com.practicum.playlistmaker.search.ui.TrackListAdapter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaLibraryUIModule = module {
    factory { TrackListAdapter() }

    viewModel {
        FavouritesTracksViewModel(get())

    }

    viewModel {
        PlaylistsViewModel(get())
    }


    viewModel {
        NewPlaylistViewModel(interactor = get())
    }

    factory { TrackModel }

    single { PlaylistAdapter(get()) }
}
