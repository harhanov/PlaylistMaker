package com.practicum.playlistmaker.media_library.di

import com.practicum.playlistmaker.media_library.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.media_library.favourites.data.TracksRepositoryImpl
import com.practicum.playlistmaker.media_library.favourites.domain.TracksRepository
import com.practicum.playlistmaker.media_library.favourites.domain.TracksInteractor
import com.practicum.playlistmaker.media_library.favourites.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.media_library.playlists.data.PlaylistRepositoryImpl
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistInteractor
import com.practicum.playlistmaker.media_library.playlists.domain.PlaylistRepository
import com.practicum.playlistmaker.media_library.playlists.domain.impl.PlaylistInteractorImpl
import org.koin.dsl.module

val mediaLibraryDomainModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }
    single<TracksRepository> { TracksRepositoryImpl(get(), get()) }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
    single<PlaylistRepository> { PlaylistRepositoryImpl(get(), get(), get()) }

    single { get<PlaylistsDatabase>().getPlaylistDao() }

}