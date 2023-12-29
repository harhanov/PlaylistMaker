package com.practicum.playlistmaker.media_library.di

import androidx.room.Room
import com.practicum.playlistmaker.media_library.data.converters.PlaylistDBConverter
import com.practicum.playlistmaker.media_library.data.converters.TrackDBConverter
import com.practicum.playlistmaker.media_library.data.db.PlaylistsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mediaLibraryDatabaseModule = module {

    single {
        Room.databaseBuilder(androidContext(), PlaylistsDatabase::class.java, "playlists_table")
            .build()
    }

    single { TrackDBConverter() }

    single { PlaylistDBConverter() }

}