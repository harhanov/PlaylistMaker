package com.practicum.playlistmaker.media_library.di

import androidx.room.Room
import com.practicum.playlistmaker.media_library.data.db.FavouritesDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val favouritesDatabaseModule = module {

    single {
        Room.databaseBuilder(androidContext(), FavouritesDatabase::class.java, "favourites_table")
            .build()
    }
}